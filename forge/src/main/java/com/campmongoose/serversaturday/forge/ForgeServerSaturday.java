package com.campmongoose.serversaturday.forge;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.SubmissionsFileStorage;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.common.submission.Submitter.Serializer;
import com.campmongoose.serversaturday.forge.command.sscommand.SSCommand;
import com.campmongoose.serversaturday.forge.command.sscommand.SSGiveReward;
import com.campmongoose.serversaturday.forge.command.sscommand.SSReload;
import com.campmongoose.serversaturday.forge.command.sscommand.SSView;
import com.campmongoose.serversaturday.forge.command.sscommand.SSViewAll;
import com.campmongoose.serversaturday.forge.command.sscommand.submit.SSEdit;
import com.campmongoose.serversaturday.forge.command.sscommand.submit.SSGetRewards;
import com.campmongoose.serversaturday.forge.network.SSNetwork;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mongodb.client.model.Filters;
import io.leangen.geantyref.TypeToken;
import io.musician101.musicianlibrary.java.configurate.ConfigurateLoader;
import io.musician101.musicianlibrary.java.minecraft.common.Location;
import io.musician101.musicianlibrary.java.storage.DataStorage;
import io.musician101.musicianlibrary.java.storage.database.mongo.MongoDataStorage;
import io.musician101.musicianlibrary.java.storage.database.sql.MySQLDataStorage;
import io.musician101.musicianlibrary.java.storage.database.sql.SQLiteDataStorage;
import java.io.File;
import javax.annotation.Nonnull;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.command.arguments.GameProfileArgument.IProfileProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

@Mod(Reference.ID)
public class ForgeServerSaturday implements ServerSaturday<ForgeRewardGiver, ITextComponent> {

    public static final Logger LOGGER = LogManager.getLogger();

    private ForgeConfig config;
    private ForgeRewardGiver rewardGiver;
    private DataStorage<?, Submitter<ITextComponent>> submissions;

    public ForgeServerSaturday() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ForgeServerSaturday getInstance() {
        return ModList.get().<ForgeServerSaturday>getModObjectById(Reference.ID).orElseThrow(() -> new IllegalStateException("ServerSaturday is not enabled!"));
    }

    @SubscribeEvent
    public void commands(RegisterCommandsEvent event) {
        String fullPrefix = Reference.ID;
        String shortPrefix = Commands.SS_CMD.replace("/", "");
        registerCommand(event, fullPrefix, shortPrefix, 0, false, new SSCommand());
        registerCommand(event, fullPrefix + Commands.EDIT_NAME, shortPrefix + Commands.EDIT_NAME, 0, true, new SSEdit());
        registerCommand(event, fullPrefix + Commands.VIEW_NAME, shortPrefix + Commands.VIEW_NAME, 0, true, new SSView());
        registerCommand(event, fullPrefix + Commands.VIEW_ALL_NAME, shortPrefix + Commands.VIEW_ALL_NAME, 3, true, new SSViewAll());
        registerCommand(event, fullPrefix + Commands.GIVE_REWARD_NAME, shortPrefix + Commands.GIVE_REWARD_NAME, 3, false, new SSGiveReward(), net.minecraft.command.Commands.argument(Commands.PLAYER, GameProfileArgument.gameProfile()));
        registerCommand(event, fullPrefix + Commands.GET_REWARDS_NAME, shortPrefix + Commands.GET_REWARDS_NAME, 0, true, new SSGetRewards());
        registerCommand(event, fullPrefix + Commands.RELOAD_NAME, shortPrefix + Commands.RELOAD_NAME, 3, false, new SSReload());
    }

    @Nonnull
    public ForgeConfig getConfig() {
        return config;
    }

    @Nonnull
    @Override
    public ForgeRewardGiver getRewardGiver() {
        return rewardGiver;
    }

    @Nonnull
    @Override
    public DataStorage<?, Submitter<ITextComponent>> getSubmissions() {
        return submissions;
    }

    private LiteralArgumentBuilder<CommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info(Messages.LOADING_CONFIG);
        config = new ForgeConfig();
        LOGGER.info(Messages.CONFIG_LOADED);
        LOGGER.info(Messages.LOADING_SUBMISSIONS);
        File configDir = new File("config/" + Reference.ID);
        File submittersDir = new File(configDir, "submitters");
        TypeToken<Submitter<ITextComponent>> typeToken = new TypeToken<Submitter<ITextComponent>>() {

        };
        Serializer<ITextComponent> submitterSerializer = new Submitter.Serializer<>(ITextComponent.class, new SQLTextComponentSerializer());
        TypeSerializerCollection tsc = TypeSerializerCollection.builder().register(typeToken, submitterSerializer).register(new TypeToken<Build<ITextComponent>>() {

        }, new Build.Serializer<>(ITextComponent.class)).register(Location.class, new Location.Serializer()).build();
        switch (config.getFormat()) {
            case Config.HOCON:
                submissions = new SubmissionsFileStorage<>(submittersDir, ConfigurateLoader.HOCON, ".conf", typeToken, tsc);
                break;
            case Config.JSON:
                submissions = new SubmissionsFileStorage<>(submittersDir, ConfigurateLoader.JSON, ".json", typeToken, tsc);
                break;
            case Config.MONGO_DB:
                submissions = new MongoDataStorage<>(config.getDatabaseOptions(), submitterSerializer, submitter -> Filters.eq(Config.UUID, submitter.getUUID()));
                break;
            case Config.MYSQL:
                submissions = new MySQLDataStorage<>(config.getDatabaseOptions(), submitterSerializer);
                break;
            case Config.SQLITE:
                submissions = new SQLiteDataStorage<>(configDir, submitterSerializer);
                break;
            case Config.YAML:
            default:
                submissions = new SubmissionsFileStorage<>(submittersDir, ConfigurateLoader.YAML, ".yml", typeToken, tsc);
        }
        LOGGER.info(Messages.SUBMISSIONS_LOADED);
        rewardGiver = new ForgeRewardGiver();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        SSNetwork.init();
    }

    private void registerCommand(RegisterCommandsEvent event, String name, String alias, int permissionLevel, boolean playerOnly, Command<CommandSource> command, RequiredArgumentBuilder<CommandSource, IProfileProvider> argument) {
        LiteralArgumentBuilder<CommandSource> cmd = literal(alias).requires(source -> {
            if (playerOnly) {
                try {
                    source.asPlayer();
                }
                catch (CommandSyntaxException e) {
                    return false;
                }
            }

            return source.hasPermissionLevel(permissionLevel);
        });
        if (argument == null) {
            cmd.executes(command);
        }
        else {
            cmd.then(argument).executes(command);
        }

        event.getDispatcher().register(literal(name).redirect(cmd.build()));
    }

    private void registerCommand(RegisterCommandsEvent event, String name, String alias, int permissionLevel, boolean playerOnly, Command<CommandSource> command) {
        registerCommand(event, name, alias, permissionLevel, playerOnly, command, null);
    }
}
