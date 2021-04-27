package com.campmongoose.serversaturday.spigot;

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
import com.campmongoose.serversaturday.spigot.gui.chest.AllSubmissionsGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.SubmitterGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.SubmittersGUI;
import com.campmongoose.serversaturday.spigot.textinput.SpigotTextInput;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mongodb.client.model.Filters;
import io.leangen.geantyref.TypeToken;
import io.musician101.bukkitier.Bukkitier;
import io.musician101.musicianlibrary.java.configurate.ConfigurateLoader;
import io.musician101.musicianlibrary.java.minecraft.common.Location;
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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import static io.musician101.bukkitier.Bukkitier.argument;
import static io.musician101.bukkitier.Bukkitier.literal;

public class SpigotServerSaturday extends JavaPlugin implements ServerSaturday<SpigotRewardGiver, String> {

    private final SpigotConfig config = new SpigotConfig();
    private SpigotRewardGiver rewardGiver;
    private DataStorage<?, Submitter<String>> submissions;

    public static SpigotServerSaturday instance() {
        return JavaPlugin.getPlugin(SpigotServerSaturday.class);
    }

    @Nonnull
    public SpigotConfig getPluginConfig() {
        return config;
    }

    @Nonnull
    @Override
    public SpigotRewardGiver getRewardGiver() {
        return rewardGiver;
    }

    @Nonnull
    @Override
    public DataStorage<?, Submitter<String>> getSubmissions() {
        return submissions;
    }

    @Override
    public void onDisable() {
        getLogger().info(Messages.SAVING_SUBMISSIONS);
        submissions.save();
        rewardGiver.save();
        getLogger().info(Messages.SUBMISSIONS_SAVED);
    }

    @Override
    public void onEnable() {
        getLogger().info(Messages.LOADING_CONFIG);
        config.reload();
        getLogger().info(Messages.CONFIG_LOADED);
        getLogger().info(Messages.LOADING_SUBMISSIONS);
        File submittersDir = new File(getDataFolder(), "submitters");
        TypeToken<Submitter<String>> typeToken = new TypeToken<Submitter<String>>() {

        };
        TypeSerializerCollection tsc = TypeSerializerCollection.builder().register(typeToken, new Submitter.Serializer<>(String.class)).register(new TypeToken<Build<String>>() {

        }, new Build.Serializer<>(String.class)).register(Location.class, new Location.Serializer()).build();
        Function<Statement, List<Submitter<String>>> deserializer = statement -> {
            List<Submitter<String>> submitters = new ArrayList<>();
            try {
                statement.addBatch(Database.CREATE_TABLE);
                statement.addBatch(Database.SELECT_TABLE);
                statement.executeBatch();
                ResultSet rs = statement.getResultSet();
                while (rs.next()) {
                    UUID uuid = UUID.fromString(rs.getString(Database.UUID));
                    String name = rs.getString(Database.USERNAME);
                    Optional<Submitter<String>> submitterOptional = submissions.getEntry(s -> uuid.equals(s.getUUID()));
                    Submitter<String> submitter;
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
                    Build<String> build = submitter.newBuild(buildName, location);
                    build.setFeatured(rs.getBoolean(Database.FEATURED));
                    build.setSubmitted(rs.getBoolean(Database.SUBMITTED));
                    build.setDescription(getList(rs.getString(Database.DESCRIPTION)));
                    build.setResourcePacks(getList(rs.getString(Database.RESOURCE_PACKS)));
                }

                rs.close();
            }
            catch (SQLException e) {
                getLogger().log(Level.WARNING, Messages.SQL_EXCEPTION, e);
            }

            return submitters;
        };
        Function<List<Submitter<String>>, List<String>> serializer = data -> {
            List<String> queries = new ArrayList<>();
            queries.add(Database.CLEAR_TABLE);
            queries.add(Database.CREATE_TABLE);
            data.stream().flatMap(entry -> entry.getBuilds().stream().map(build -> Database.addBuild(entry, build, strings -> {
                JsonArray jsonArray = new JsonArray();
                strings.forEach(jsonArray::add);
                return new Gson().toJson(jsonArray);
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
                submissions = new MongoDataStorage<>(config.getDatabaseOptions(), new Submitter.Serializer<>(String.class), submitter -> Filters.eq(Config.UUID, submitter.getUUID()));
                break;
            case Config.MYSQL:
                submissions = new MySQLDataStorage<>(config.getDatabaseOptions(), deserializer, serializer);
                break;
            case Config.SQLITE:
                submissions = new SQLiteDataStorage<>(getDataFolder(), deserializer, serializer);
                break;
            case Config.YAML:
            default:
                submissions = new SubmissionsFileStorage<>(submittersDir, ConfigurateLoader.YAML, ".yml", typeToken, tsc);
        }
        getLogger().info(Messages.SUBMISSIONS_LOADED);
        getServer().getPluginManager().registerEvents(new SpigotTextInput(), this);
        rewardGiver = new SpigotRewardGiver();
        registerCommands();
    }

    private List<String> getList(String string) {
        return StreamSupport.stream(new Gson().fromJson(string, JsonArray.class).spliterator(), false).map(JsonElement::getAsString).collect(Collectors.toList());
    }

    private void registerCommand(LiteralArgumentBuilder<CommandSender> argumentBuilder) {
        Bukkitier.registerCommand(this, argumentBuilder);
    }

    private void registerCommands() {
        registerCommand(literal(Reference.ID).executes(context -> {
            CommandSender sender = context.getSource();
            sender.sendMessage(ChatColor.GREEN + "===== " + ChatColor.RESET + Reference.NAME + " v" + Reference.VERSION + ChatColor.GREEN + " by " + ChatColor.RESET + "Musician101" + ChatColor.GREEN + " =====");
            sender.sendMessage(Commands.SS_CMD + " " + ChatColor.AQUA + Commands.HELP_DESC);
            sender.sendMessage(Commands.SS_CMD + Commands.EDIT_NAME + " " + ChatColor.AQUA + Commands.EDIT_DESC);
            sender.sendMessage(Commands.SS_CMD + Commands.GET_REWARDS_NAME + " " + ChatColor.AQUA + Commands.GET_REWARDS_DESC);
            sender.sendMessage(Commands.SS_CMD + Commands.GIVE_REWARD_NAME + " " + ChatColor.AQUA + Commands.GIVE_REWARD_DESC);
            sender.sendMessage(Commands.SS_CMD + Commands.RELOAD_NAME + " " + ChatColor.AQUA + Commands.RELOAD_DESC);
            sender.sendMessage(Commands.SS_CMD + Commands.VIEW_NAME + " " + ChatColor.AQUA + Commands.VIEW_DESC);
            sender.sendMessage(Commands.SS_CMD + Commands.VIEW_ALL_NAME + " " + ChatColor.AQUA + Commands.VIEW_ALL_DESC);
            return 1;
        }));
        registerCommand(literal(Reference.ID + Commands.EDIT_NAME).requires(sender -> sender instanceof Player && sender.hasPermission(Permissions.SUBMIT)).executes(context -> {
            Player player = (Player) context.getSource();
            Submitter<String> submitter = getSubmissions().getEntry(s -> s.getUUID().equals(player.getUniqueId())).orElseThrow(() -> new SimpleCommandExceptionType(() -> Messages.PLAYER_NOT_FOUND).create());
            new SubmitterGUI(submitter, player);
            return 1;
        }));
        registerCommand(literal(Reference.ID + Commands.GET_REWARDS_NAME).requires(sender -> sender instanceof Player && sender.hasPermission(Permissions.SUBMIT)).executes(context -> {
            Player player = (Player) context.getSource();
            rewardGiver.givePlayerReward(player);
            player.sendMessage(ChatColor.GOLD + Messages.REWARDS_RECEIVED);
            return 1;
        }));
        registerCommand(literal(Reference.ID + Commands.GIVE_REWARD_NAME).requires(sender -> sender.hasPermission(Permissions.FEATURE)).then(argument(Commands.PLAYER, new ArgumentType<OfflinePlayer>() {

            @SuppressWarnings("deprecation")
            @Override
            public OfflinePlayer parse(StringReader stringReader) throws CommandSyntaxException {
                return getServer().getOfflinePlayer(stringReader.readString());
            }

            @Override
            public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
                String input = builder.getRemaining();
                Stream.of(getServer().getOfflinePlayers()).map(OfflinePlayer::getName).filter(Objects::nonNull).filter(s -> s.startsWith(input)).forEach(builder::suggest);
                return builder.buildFuture();
            }
        }).executes(context -> {
            OfflinePlayer offlinePlayer = context.getArgument(Commands.PLAYER, OfflinePlayer.class);
            rewardGiver.addReward(offlinePlayer.getUniqueId());
            context.getSource().sendMessage(ChatColor.GOLD + Messages.rewardsGiven(offlinePlayer.getName()));
            Player player = offlinePlayer.getPlayer();
            if (player != null) {
                player.sendMessage(ChatColor.GOLD + Messages.REWARDS_WAITING);
            }

            return 1;
        })));
        registerCommand(literal(Reference.ID + Commands.RELOAD_NAME).requires(sender -> sender.hasPermission(Permissions.RELOAD)).executes(context -> {
            getSubmissions().save();
            getSubmissions().load();
            config.reload();
            context.getSource().sendMessage(ChatColor.GOLD + Messages.PLUGIN_RELOADED);
            return 1;
        }));
        registerCommand(literal(Reference.ID + Commands.VIEW_NAME).requires(sender -> sender instanceof Player && sender.hasPermission(Permissions.VIEW)).executes(context -> {
            new SubmittersGUI((Player) context.getSource());
            return 1;
        }));
        registerCommand(literal(Reference.ID + Commands.VIEW_ALL_NAME).requires(sender -> sender instanceof Player && sender.hasPermission(Permissions.FEATURE)).executes(context -> {
            new AllSubmissionsGUI((Player) context.getSource());
            return 1;
        }));
    }
}
