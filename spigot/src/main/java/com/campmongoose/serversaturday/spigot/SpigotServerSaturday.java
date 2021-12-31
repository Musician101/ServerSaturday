package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.SubmissionsFileStorage;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.common.submission.Submitter.Serializer;
import com.campmongoose.serversaturday.spigot.gui.chest.AllSubmissionsGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.EditBuildGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.SubmitterGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.SubmittersGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.ViewBuildGUI;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.leangen.geantyref.TypeToken;
import io.musician101.bukkitier.Bukkitier;
import io.musician101.musicianlibrary.java.configurate.ConfigurateLoader;
import io.musician101.musicianlibrary.java.minecraft.common.Location;
import io.musician101.musicianlibrary.java.storage.DataStorage;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import static io.musician101.bukkitier.Bukkitier.argument;
import static io.musician101.bukkitier.Bukkitier.literal;

public class SpigotServerSaturday extends JavaPlugin implements ServerSaturday<SpigotRewardGiver> {

    private final SpigotConfig config = new SpigotConfig();
    private SpigotRewardGiver rewardGiver;
    private DataStorage<?, Submitter> submissions;

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
    public DataStorage<?, Submitter> getSubmissions() {
        return submissions;
    }

    @Override
    public void onDisable() {
        getLogger().info(Messages.SAVING_SUBMISSIONS);
        submissions.save().forEach((file, e) -> {
            getLogger().warning("Error in " + ((File) file).getName());
            e.printStackTrace();
        });
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
        TypeToken<Submitter> typeToken = TypeToken.get(Submitter.class);
        Serializer submitterSerializer = new Submitter.Serializer();
        TypeSerializerCollection tsc = TypeSerializerCollection.builder().register(typeToken, submitterSerializer).register(TypeToken.get(Build.class), new Build.Serializer()).register(Location.class, new Location.Serializer()).build();
        submissions = new SubmissionsFileStorage(submittersDir, ConfigurateLoader.YAML, ".yml", typeToken, tsc);
        /*switch (config.getFormat()) {
            case Config.HOCON -> submissions = new SubmissionsFileStorage(submittersDir, ConfigurateLoader.HOCON, ".conf", typeToken, tsc);
            case Config.JSON -> submissions = new SubmissionsFileStorage(submittersDir, ConfigurateLoader.JSON, ".json", typeToken, tsc);
            case Config.MONGO_DB -> submissions = new MongoDataStorage<>(config.getDatabaseOptions(), submitterSerializer, submitter -> Filters.eq(Config.UUID, submitter.getUUID()));
            case Config.MYSQL -> submissions = new MySQLDataStorage<>(config.getDatabaseOptions(), submitterSerializer);
            case Config.SQLITE -> submissions = new SQLiteDataStorage<>(getDataFolder(), submitterSerializer);
            default -> submissions = new SubmissionsFileStorage(submittersDir, ConfigurateLoader.YAML, ".yml", typeToken, tsc);
        }*/

        getLogger().info(Messages.SUBMISSIONS_LOADED);
        rewardGiver = new SpigotRewardGiver();
        registerCommands();
    }

    private void registerCommand(LiteralArgumentBuilder<CommandSender> argumentBuilder, String... aliases) {
        Bukkitier.registerCommand(this, argumentBuilder, aliases);
    }

    private void registerCommands() {
        registerCommand(literal(Reference.ID).executes(context -> {
            CommandSender sender = context.getSource();
            sender.sendMessage(ChatColor.GREEN + "===== " + ChatColor.RESET + Reference.NAME + " v" + Reference.VERSION + ChatColor.GREEN + " by " + ChatColor.RESET + "Musician101" + ChatColor.GREEN + " =====");
            String baseCMD = "/" + Commands.SS_CMD;
            sender.sendMessage(baseCMD + " " + ChatColor.AQUA + Commands.HELP_DESC);
            if (sender.hasPermission(Permissions.SUBMIT)) {
                sender.sendMessage(baseCMD + Commands.EDIT_NAME + " " + ChatColor.AQUA + Commands.EDIT_DESC);
                sender.sendMessage(baseCMD + Commands.GET_REWARDS_NAME + " " + ChatColor.AQUA + Commands.GET_REWARDS_DESC);
            }

            if (sender.hasPermission(Permissions.FEATURE)) {
                sender.sendMessage(baseCMD + Commands.GIVE_REWARD_NAME + " " + ChatColor.AQUA + Commands.GIVE_REWARD_DESC);
                sender.sendMessage(baseCMD + Commands.VIEW_ALL_NAME + " " + ChatColor.AQUA + Commands.VIEW_ALL_DESC);
            }

            if (sender.hasPermission(Permissions.RELOAD)) {
                sender.sendMessage(baseCMD + Commands.RELOAD_NAME + " " + ChatColor.AQUA + Commands.RELOAD_DESC);
            }

            if (sender.hasPermission(Permissions.VIEW)) {
                sender.sendMessage(baseCMD + Commands.VIEW_NAME + " " + ChatColor.AQUA + Commands.VIEW_DESC);
            }
            return 1;
        }), Commands.SS_CMD);
        registerCommand(literal(Reference.ID + Commands.EDIT_NAME).requires(sender -> sender instanceof Player && sender.hasPermission(Permissions.SUBMIT)).executes(context -> {
            Player player = (Player) context.getSource();
            Submitter submitter = getSubmissions().getEntry(e -> e.getUUID().equals(player.getUniqueId())).orElseGet(() -> {
                Submitter newSubmitter = new Submitter(player.getName(), player.getUniqueId());
                getSubmissions().addEntry(newSubmitter);
                return newSubmitter;
            });

            new SubmitterGUI(submitter, player);
            return 1;
        }).then(argument(Commands.BUILD, new BuildArgumentType()).executes(context -> {
            Player player = (Player) context.getSource();
            Submitter submitter = getSubmissions().getEntry(e -> e.getUUID().equals(player.getUniqueId())).orElseGet(() -> {
                Submitter newSubmitter = new Submitter(player.getName(), player.getUniqueId());
                getSubmissions().addEntry(newSubmitter);
                return newSubmitter;
            });

            Build build = (Build) context.getArgument(Commands.BUILD, Map.class).get(submitter.getUUID());
            if (build == null) {
                player.sendMessage(ChatColor.RED + Messages.BUILD_DOES_NOT_EXIST);
                return 0;
            }

            new EditBuildGUI(build, submitter, player);
            return 1;
        })), Commands.SS_CMD + Commands.EDIT_NAME);
        registerCommand(Reference.ID + Commands.GET_REWARDS_NAME, new PermissionCheckExecutor() {

            @Override
            public boolean hasPermission(CommandSender sender) {
                return sender instanceof Player && sender.hasPermission(Permissions.SUBMIT);
            }

            @Override
            public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings) {
                if (!hasPermission(commandSender)) {
                    commandSender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION);
                    return false;
                }

                Player player = (Player) commandSender;
                rewardGiver.givePlayerReward(player);
                player.sendMessage(ChatColor.GOLD + Messages.REWARDS_RECEIVED);
                return true;
            }
        });
        registerCommand(Reference.ID + Commands.GIVE_REWARD_NAME, new PermissionCheckExecutor() {

            @Override
            public boolean hasPermission(CommandSender sender) {
                return sender.hasPermission(Permissions.FEATURE);
            }

            @SuppressWarnings("deprecation")
            @Override
            public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings) {
                if (!hasPermission(commandSender)) {
                    commandSender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION);
                    return false;
                }

                if (strings.length == 0) {
                    commandSender.sendMessage(ChatColor.RED + Messages.PREFIX + "Not enough arguments.");
                }

                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(strings[1]);
                rewardGiver.addReward(offlinePlayer.getUniqueId());
                commandSender.sendMessage(ChatColor.GOLD + Messages.rewardsGiven(offlinePlayer.getName()));
                Player player = offlinePlayer.getPlayer();
                if (player != null) {
                    player.sendMessage(ChatColor.GOLD + Messages.REWARDS_WAITING);
                }
                return false;
            }

            @Override
            public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings) {
                Stream<String> offlinePlayers = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName);
                if (strings.length > 0) {
                    offlinePlayers = offlinePlayers.filter(offlinePlayer -> offlinePlayer.startsWith(strings[1]));
                }

                return offlinePlayers.collect(Collectors.toList());
            }
        });
        registerCommand(Reference.ID + Commands.RELOAD_NAME, new PermissionCheckExecutor() {

            @Override
            public boolean hasPermission(CommandSender sender) {
                return sender.hasPermission(Permissions.RELOAD);
            }

            @Override
            public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings) {
                if (!hasPermission(commandSender)) {
                    commandSender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION);
                    return false;
                }

                getSubmissions().save();
                getSubmissions().load();
                config.reload();
                commandSender.sendMessage(ChatColor.GOLD + Messages.PLUGIN_RELOADED);
                return true;
            }
        });
        registerCommand(Reference.ID + Commands.VIEW_NAME, new PermissionCheckExecutor() {

            @Override
            public boolean hasPermission(CommandSender sender) {
                return sender instanceof Player && sender.hasPermission(Permissions.VIEW);
            }

            @Override
            public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings) {
                if (!hasPermission(commandSender)) {
                    commandSender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION);
                    return false;
                }

                Player player = (Player) commandSender;
                if (strings.length > 0) {
                    Optional<Submitter> optional = submissions.getEntry(e -> e.getName().equalsIgnoreCase(strings[0]));
                    if (optional.isEmpty()) {
                        player.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_FOUND);
                        return false;
                    }

                    Submitter submitter = optional.get();
                    if (strings.length > 1) {
                        Build build = submitter.getBuild(Arrays.stream(strings).filter(string -> string.equalsIgnoreCase(submitter.getName())).collect(Collectors.joining(" ")));
                        if (build == null) {
                            player.sendMessage(Messages.BUILD_DOES_NOT_EXIST);
                            return false;
                        }

                        new ViewBuildGUI(build, submitter, player);
                        return true;
                    }

                    new SubmitterGUI(submitter, player);
                    return true;
                }

                new SubmittersGUI(player);
                return true;
            }

            @Override
            public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings) {
                if (strings.length == 1) {
                    return submissions.getData().stream().map(Submitter::getName).filter(submitter -> submitter.startsWith(strings[0])).collect(Collectors.toList());
                }
                else if (strings.length > 1) {
                    Optional<Submitter> optional = submissions.getEntry(e -> e.getName().equalsIgnoreCase(strings[0]));
                    if (optional.isEmpty()) {
                        return Collections.emptyList();
                    }

                    Submitter submitter = optional.get();
                    return submitter.getBuilds().stream().map(Build::getName).filter(build -> build.equalsIgnoreCase(Arrays.stream(strings).filter(string -> string.equalsIgnoreCase(submitter.getName())).collect(Collectors.joining(" ")))).collect(Collectors.toList());
                }

                return submissions.getData().stream().map(Submitter::getName).collect(Collectors.toList());
            }
        });
        registerCommand(Reference.ID + Commands.VIEW_ALL_NAME, new PermissionCheckExecutor() {

            @Override
            public boolean hasPermission(CommandSender sender) {
                return sender instanceof Player && sender.hasPermission(Permissions.FEATURE);
            }

            @Override
            public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings) {
                if (!hasPermission(commandSender)) {
                    commandSender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION);
                    return false;
                }

                new AllSubmissionsGUI((Player) commandSender);
                return true;
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    private void registerCommand(String name, PermissionCheckExecutor executor) {
        PluginCommand command = getCommand(name);
        command.setExecutor(executor);
        command.setTabCompleter(executor);
    }
}
