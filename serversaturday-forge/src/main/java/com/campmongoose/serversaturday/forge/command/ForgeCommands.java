package com.campmongoose.serversaturday.forge.command;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.forge.command.args.BuildArgumentType;
import com.campmongoose.serversaturday.forge.command.args.SubmitterArgumentType;
import com.campmongoose.serversaturday.forge.command.sscommand.SSCommand;
import com.campmongoose.serversaturday.forge.command.sscommand.SSReload;
import com.campmongoose.serversaturday.forge.command.sscommand.feature.SSFeature;
import com.campmongoose.serversaturday.forge.command.sscommand.feature.SSGiveReward;
import com.campmongoose.serversaturday.forge.command.sscommand.submit.SSDescription;
import com.campmongoose.serversaturday.forge.command.sscommand.submit.SSEdit;
import com.campmongoose.serversaturday.forge.command.sscommand.submit.SSGetRewards;
import com.campmongoose.serversaturday.forge.command.sscommand.submit.SSLocation;
import com.campmongoose.serversaturday.forge.command.sscommand.submit.SSNew;
import com.campmongoose.serversaturday.forge.command.sscommand.submit.SSRemove;
import com.campmongoose.serversaturday.forge.command.sscommand.submit.SSRename;
import com.campmongoose.serversaturday.forge.command.sscommand.submit.SSResourcePack;
import com.campmongoose.serversaturday.forge.command.sscommand.submit.SSSubmit;
import com.campmongoose.serversaturday.forge.command.sscommand.view.SSGoto;
import com.campmongoose.serversaturday.forge.command.sscommand.view.SSView;
import com.campmongoose.serversaturday.forge.command.sscommand.view.SSViewDescription;
import com.campmongoose.serversaturday.forge.command.sscommand.view.SSViewResourcePack;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public class ForgeCommands {

    private ForgeCommands() {

    }

    public static void init() {
        String fullPrefix = Reference.ID.replace("_", "");
        String shortPrefix = Commands.SS_CMD.replace("/", "");
        RequiredArgumentBuilder<CommandSource, String> buildArgument = RequiredArgumentBuilder.argument(Commands.BUILD, new BuildArgumentType());
        RequiredArgumentBuilder<CommandSource, ForgeSubmitter> submitterArgument = RequiredArgumentBuilder.<CommandSource, ForgeSubmitter>argument("submitter", new SubmitterArgumentType()).then(buildArgument);
        Predicate<CommandSource> playerOnly = source -> {
            try {
                source.asPlayer();
                return true;
            }
            catch (CommandSyntaxException e) {
                source.sendFeedback(new StringTextComponent(Messages.PLAYER_ONLY).applyTextStyle(TextFormatting.RED), true);
                return false;
            }
        };
        Predicate<CommandSource> hasPermission = source -> source.hasPermissionLevel(3);
        register(fullPrefix, shortPrefix, new SSCommand(), source -> true, null);
        register(fullPrefix + Commands.DESCRIPTION_NAME, shortPrefix + "d", new SSDescription(), playerOnly, buildArgument);
        register(fullPrefix + Commands.EDIT_NAME, shortPrefix + "e", new SSEdit(), playerOnly, buildArgument);
        register(fullPrefix + Commands.LOCATION_NAME, shortPrefix + "l", new SSLocation(), playerOnly, buildArgument);
        register(fullPrefix + Commands.NEW_NAME, shortPrefix + "n", new SSNew(), playerOnly, buildArgument);
        register(fullPrefix + Commands.REMOVE_NAME, shortPrefix + "rem", new SSRemove(), playerOnly, buildArgument);
        register(fullPrefix + Commands.RENAME_NAME, shortPrefix + "ren", new SSRename(), playerOnly, buildArgument);
        register(fullPrefix + Commands.RESOURCE_PACK_NAME, shortPrefix + "rp", new SSResourcePack(), playerOnly, buildArgument);
        register(fullPrefix + Commands.SUBMIT_NAME, shortPrefix + "s", new SSSubmit(), playerOnly, buildArgument);
        register(fullPrefix + Commands.GOTO_NAME, shortPrefix + "g", new SSGoto(), playerOnly, submitterArgument);
        register(fullPrefix + Commands.VIEW_NAME, shortPrefix + "v", new SSView(), playerOnly, submitterArgument);
        register(fullPrefix + Commands.VIEW_DESCRIPTION_NAME, shortPrefix + "vd", new SSViewDescription(), playerOnly, submitterArgument);
        register(fullPrefix + Commands.VIEW_DESCRIPTION_NAME, shortPrefix + "vrp", new SSViewResourcePack(), playerOnly, submitterArgument);
        register(fullPrefix + Commands.FEATURE_NAME, shortPrefix + "f", new SSFeature(), playerOnly.and(hasPermission), submitterArgument);
        register(fullPrefix + Commands.GIVE_REWARD_NAME, shortPrefix + "gr", new SSGiveReward(), hasPermission, net.minecraft.command.Commands.argument(Commands.PLAYER, GameProfileArgument.gameProfile()));
        register(fullPrefix + Commands.GIVE_REWARD_NAME, shortPrefix + "getr", new SSGetRewards(), hasPermission, null);
        register(fullPrefix + Commands.RELOAD_NAME, shortPrefix + "r", new SSReload(), source -> source.hasPermissionLevel(3), null);
    }

    private static LiteralArgumentBuilder<CommandSource> literal(String name) {
        return net.minecraft.command.Commands.literal(name);
    }

    private static void register(@Nonnull String fullPrefix, @Nonnull String shortPrefix, @Nonnull ForgeCommand command, @Nonnull Predicate<CommandSource> requires, @Nullable RequiredArgumentBuilder<CommandSource, ?> arguments) {
        CommandDispatcher<CommandSource> dispatcher = LogicalSidedProvider.INSTANCE.<MinecraftServer>get(LogicalSide.SERVER).getCommandManager().getDispatcher();
        LiteralArgumentBuilder<CommandSource> builder = literal(fullPrefix).executes(command).requires(requires);
        if (arguments != null) {
            builder.then(arguments);
        }

        LiteralCommandNode<CommandSource> literal = dispatcher.register(builder);
        dispatcher.register(literal(shortPrefix).redirect(literal));
    }

}
