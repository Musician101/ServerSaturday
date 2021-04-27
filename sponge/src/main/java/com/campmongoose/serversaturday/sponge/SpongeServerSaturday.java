package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Database;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.SubmissionsFileStorage;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.command.SSCommand;
import com.campmongoose.serversaturday.sponge.command.SSGiveReward;
import com.campmongoose.serversaturday.sponge.command.SSReload;
import com.campmongoose.serversaturday.sponge.command.SSView;
import com.campmongoose.serversaturday.sponge.command.SSViewAll;
import com.campmongoose.serversaturday.sponge.command.submit.SSEdit;
import com.campmongoose.serversaturday.sponge.command.submit.SSGetRewards;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.mongodb.client.model.Filters;
import io.leangen.geantyref.TypeToken;
import io.musician101.musicianlibrary.java.configurate.ConfigurateLoader;
import io.musician101.musicianlibrary.java.minecraft.common.Location;
import io.musician101.musicianlibrary.java.minecraft.sponge.gui.book.SpongeBookGUI;
import io.musician101.musicianlibrary.java.storage.DataStorage;
import io.musician101.musicianlibrary.java.storage.database.mongo.MongoDataStorage;
import io.musician101.musicianlibrary.java.storage.database.sql.MySQLDataStorage;
import io.musician101.musicianlibrary.java.storage.database.sql.SQLiteDataStorage;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.RegisterDataEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.Plugin;

import static net.kyori.adventure.text.Component.text;

@Plugin(Reference.ID)
public class SpongeServerSaturday implements ServerSaturday<SpongeRewardGiver, Component> {

    @Nonnull
    private final SpongeConfig config;
    @Nonnull
    private final PluginContainer pluginContainer;
    private SpongeRewardGiver rewardGiver;
    private DataStorage<?, Submitter<Component>> submissions;

    @Inject
    public SpongeServerSaturday(@Nonnull PluginContainer pluginContainer, @DefaultConfig(sharedRoot = false) ConfigurationReference<ConfigurationNode> configReference) {
        this.pluginContainer = pluginContainer;
        config = new SpongeConfig(configReference);
    }

    public static SpongeServerSaturday instance() {
        return Sponge.pluginManager().plugin(Reference.ID).map(PluginContainer::getInstance).filter(SpongeServerSaturday.class::isInstance).map(SpongeServerSaturday.class::cast).orElseThrow(() -> new IllegalStateException("ServerSaturday is not enabled!"));
    }

    @Nonnull
    public SpongeConfig getConfig() {
        return config;
    }

    @Nonnull
    public Logger getLogger() {
        return pluginContainer.getLogger();
    }

    @Nonnull
    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }

    @Nonnull
    @Override
    public SpongeRewardGiver getRewardGiver() {
        return rewardGiver;
    }

    @Nonnull
    @Override
    public DataStorage<?, Submitter<Component>> getSubmissions() {
        return submissions;
    }

    @Listener
    public void stopping(StoppingEngineEvent<Server> event) {
        getLogger().info(Messages.SAVING_SUBMISSIONS);
        submissions.save();
        getLogger().info(Messages.SUBMISSIONS_SAVED);
    }

    @Listener
    public void starting(StartingEngineEvent<Server> event) {
        getLogger().info(Messages.LOADING_CONFIG);
        getLogger().info(Messages.CONFIG_LOADED);
        getLogger().info(Messages.LOADING_SUBMISSIONS);
        pluginContainer.getPath();
        File configDir = new File(Sponge.game().gameDirectory().toFile(), Reference.ID);
        File submittersDir = new File(configDir, "submitters");
        TypeToken<Submitter<Component>> typeToken = new TypeToken<Submitter<Component>>() {

        };
        TypeSerializerCollection tsc = TypeSerializerCollection.builder().register(typeToken, new Submitter.Serializer<>(Component.class)).register(new TypeToken<Build<Component>>() {

        }, new Build.Serializer<>(Component.class)).register(Location.class, new Location.Serializer()).build();
        Function<Statement, List<Submitter<Component>>> deserializer = statement -> {
            List<Submitter<Component>> submitters = new ArrayList<>();
            try {
                statement.addBatch(Database.CREATE_TABLE);
                statement.addBatch(Database.SELECT_TABLE);
                statement.executeBatch();
                ResultSet rs = statement.getResultSet();
                while (rs.next()) {
                    UUID uuid = UUID.fromString(rs.getString(Database.UUID));
                    String name = rs.getString(Database.USERNAME);
                    Optional<Submitter<Component>> submitterOptional = submissions.getEntry(s -> uuid.equals(s.getUUID()));
                    Submitter<Component> submitter;
                    if (submitterOptional.isPresent()) {
                        submitter = submitterOptional.get();
                    }
                    else {
                        submitter = new Submitter<>(name, uuid);
                        submissions.addEntry(submitter);
                    }

                    String buildName = rs.getString(Database.BUILD_NAME);
                    String world = rs.getString(Database.WORLD_NAME);
                    double x = rs.getDouble(Database.X);
                    double y = rs.getDouble(Database.Y);
                    double z = rs.getDouble(Database.Z);
                    Location location = new Location(world, x, y, z);
                    Build<Component> build = submitter.newBuild(buildName, location);
                    build.setFeatured(rs.getBoolean(Database.FEATURED));
                    build.setSubmitted(rs.getBoolean(Database.SUBMITTED));
                    build.setDescription(getList(rs.getString(Database.DESCRIPTION)));
                    build.setResourcePacks(getList(rs.getString(Database.RESOURCE_PACKS)));
                }

                rs.close();
            }
            catch (SQLException e) {
                getLogger().warn(Messages.SQL_EXCEPTION, e);
            }

            return submitters;
        };
        Function<List<Submitter<Component>>, List<String>> serializer = data -> {
            List<String> queries = new ArrayList<>();
            queries.add(Database.CLEAR_TABLE);
            queries.add(Database.CREATE_TABLE);
            GsonComponentSerializer gcs = GsonComponentSerializer.gson();
            Gson gson = gcs.serializer();
            data.stream().flatMap(entry -> entry.getBuilds().stream().map(build -> Database.addBuild(entry, build, strings -> {
                JsonArray jsonArray = new JsonArray();
                strings.stream().map(gcs::serializeToTree).forEach(jsonArray::add);
                return gson.toJson(jsonArray);
            }))).forEach(queries::add);
            return queries;
        };
        switch (config.getFormat()) {
            case Config.HOCON:
                submissions = new SubmissionsFileStorage<>(submittersDir, ConfigurateLoader.HOCON, ".conf", typeToken, tsc);
                break;
            case Config.JSON:
                submissions = new SubmissionsFileStorage<>(submittersDir, ConfigurateLoader.JSON, ".json", typeToken, tsc);
                break;
            case Config.MONGO_DB:
                submissions = new MongoDataStorage<>(config.getDatabaseOptions(), new Submitter.Serializer<>(Component.class), submitter -> Filters.eq(Config.UUID, submitter.getUUID()));
                break;
            case Config.MYSQL:
                submissions = new MySQLDataStorage<>(config.getDatabaseOptions(), deserializer, serializer);
                break;
            case Config.SQLITE:
                submissions = new SQLiteDataStorage<>(configDir, deserializer, serializer);
                break;
            case Config.YAML:
            default:
                submissions = new SubmissionsFileStorage<>(submittersDir, ConfigurateLoader.YAML, ".yml", typeToken, tsc);
        }

        getLogger().info(Messages.SUBMISSIONS_LOADED);
        rewardGiver = new SpongeRewardGiver(new File(configDir, "rewards.conf"), ConfigurateLoader.HOCON);
    }

    private List<Component> getList(String string) {
        GsonComponentSerializer gcs = GsonComponentSerializer.gson();
        Gson gson = gcs.serializer();
        return StreamSupport.stream(gson.fromJson(string, JsonArray.class).spliterator(), false).map(JsonElement::toString).map(gcs::deserialize).collect(Collectors.toList());
    }

    @Listener
    public void data(RegisterDataEvent event) {
        SpongeBookGUI.init(pluginContainer, event);
    }

    @Listener
    public void commands(RegisterCommandEvent<Parameterized> event) {
        String fullPrefix = Reference.ID.replace("_", "");
        String shortPrefix = Commands.SS_CMD.replace("/", "");
        Command.builder().shortDescription(text(Commands.HELP_DESC)).executor(new SSCommand());
        event.register(pluginContainer, Command.builder().shortDescription(text(Commands.HELP_DESC)).executor(new SSCommand()).build(), fullPrefix, shortPrefix);
        event.register(pluginContainer, Command.builder().shortDescription(text(Commands.EDIT_DESC)).executor(new SSEdit()).executionRequirements(cause -> cause.hasPermission(Permissions.SUBMIT) && cause.subject() instanceof ServerPlayer).build(), fullPrefix + Commands.EDIT_NAME, shortPrefix + Commands.EDIT_NAME);
        event.register(pluginContainer, Command.builder().shortDescription(text(Commands.VIEW_DESC)).executor(new SSView()).executionRequirements(cause -> cause.hasPermission(Permissions.VIEW) && cause.subject() instanceof ServerPlayer).build(), fullPrefix + Commands.VIEW_NAME, shortPrefix + Commands.VIEW_NAME);
        event.register(pluginContainer, Command.builder().shortDescription(text(Commands.VIEW_ALL_DESC)).executor(new SSViewAll()).executionRequirements(cause -> cause.hasPermission(Permissions.VIEW) && cause.subject() instanceof ServerPlayer).build(), fullPrefix + Commands.VIEW_ALL_NAME, shortPrefix + Commands.VIEW_ALL_NAME);
        event.register(pluginContainer, Command.builder().shortDescription(text(Commands.GIVE_REWARD_DESC)).addParameter(Parameter.user().key("player").build()).executor(new SSGiveReward()).permission(Permissions.FEATURE).build(), fullPrefix + Commands.GIVE_REWARD_NAME, shortPrefix + Commands.GIVE_REWARD_NAME);
        event.register(pluginContainer, Command.builder().shortDescription(text(Commands.GET_REWARDS_DESC)).executor(new SSGetRewards()).executionRequirements(cause -> cause.hasPermission(Permissions.SUBMIT) && cause.subject() instanceof ServerPlayer).build(), fullPrefix + Commands.GET_REWARDS_NAME, shortPrefix + Commands.GET_REWARDS_NAME);
        event.register(pluginContainer, Command.builder().shortDescription(text(Commands.RELOAD_DESC)).executor(new SSReload()).permission(Permissions.RELOAD).build(), fullPrefix + Commands.RELOAD_NAME, shortPrefix + Commands.RELOAD_NAME);
    }
}
