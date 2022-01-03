package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.Reference.Permissions;
import com.campmongoose.serversaturday.RewardHandler;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.gui.AllSubmissionsGUI;
import com.campmongoose.serversaturday.gui.EditBuildGUI;
import com.campmongoose.serversaturday.gui.SubmitterGUI;
import com.campmongoose.serversaturday.gui.SubmittersGUI;
import com.campmongoose.serversaturday.gui.ViewBuildGUI;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submissions;
import com.campmongoose.serversaturday.submission.Submitter;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.musician101.bukkitier.Bukkitier;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.musician101.bukkitier.Bukkitier.argument;
import static io.musician101.bukkitier.Bukkitier.literal;

public final class SSCommand {

    private SSCommand() {

    }

    private static LiteralArgumentBuilder<CommandSender> claim() {
        return literal(Commands.CLAIM_NAME).requires(sender -> sender instanceof Player && sender.hasPermission(Permissions.SUBMIT)).executes(context -> {
            Player player = (Player) context.getSource();
            getRewardHandler().claimReward(player);
            player.sendMessage(ChatColor.GOLD + Messages.REWARDS_RECEIVED);
            return 1;
        });
    }

    private static LiteralArgumentBuilder<CommandSender> edit() {
        return literal(Commands.EDIT_NAME).requires(sender -> sender instanceof Player && sender.hasPermission(Permissions.SUBMIT)).executes(context -> {
            Player player = (Player) context.getSource();
            new SubmitterGUI(getSubmitter(player), player);
            return 1;
        }).then(argument(Commands.BUILD, new BuildArgumentType()).executes(context -> {
            Player player = (Player) context.getSource();
            Submitter submitter = getSubmitter(player);
            Build build = (Build) context.getArgument(Commands.BUILD, Map.class).get(submitter.getUUID());
            if (build == null) {
                player.sendMessage(ChatColor.RED + Messages.BUILD_DOES_NOT_EXIST);
                return 0;
            }

            new EditBuildGUI(build, submitter, player);
            return 1;
        }));
    }

    private static ServerSaturday getPlugin() {
        return ServerSaturday.getInstance();
    }

    private static RewardHandler getRewardHandler() {
        return getPlugin().getRewardHandler();
    }

    private static Submissions getSubmissions() {
        return getPlugin().getSubmissions();
    }

    private static Submitter getSubmitter(Player player) {
        return getSubmissions().getSubmitter(player);
    }

    public static void registerCommand() {
        Bukkitier.registerCommand(getPlugin(), literal(Reference.ID).executes(context -> {
            CommandSender sender = context.getSource();
            sender.sendMessage(ChatColor.GREEN + "===== " + ChatColor.RESET + Reference.NAME + " v" + Reference.VERSION + ChatColor.GREEN + " by " + ChatColor.RESET + "Musician101" + ChatColor.GREEN + " =====");
            String baseCMD = "/" + Commands.SS_CMD + " ";
            sender.sendMessage(baseCMD + ChatColor.AQUA + Commands.HELP_DESC);
            if (sender.hasPermission(Permissions.SUBMIT)) {
                sender.sendMessage(baseCMD + Commands.EDIT_NAME + " [build] " + ChatColor.AQUA + Commands.EDIT_DESC);
                sender.sendMessage(baseCMD + Commands.CLAIM_NAME + " " + ChatColor.AQUA + Commands.CLAIM_DESC);
                sender.sendMessage(baseCMD + Commands.SUBMIT_NAME + " <build> " + ChatColor.AQUA + Commands.SUBMIT_DESC);
            }

            if (sender.hasPermission(Permissions.FEATURE)) {
                sender.sendMessage(baseCMD + Commands.REWARD_NAME + " <player> " + ChatColor.AQUA + Commands.REWARD_DESC);
                sender.sendMessage(baseCMD + Commands.VIEW_ALL_NAME + " " + ChatColor.AQUA + Commands.VIEW_ALL_DESC);
            }

            if (sender.hasPermission(Permissions.ADMIN)) {
                sender.sendMessage(baseCMD + Commands.RELOAD_NAME + " " + ChatColor.AQUA + Commands.RELOAD_DESC);
            }

            if (sender.hasPermission(Permissions.VIEW)) {
                sender.sendMessage(baseCMD + Commands.VIEW_NAME + " [player] [build]" + ChatColor.AQUA + Commands.VIEW_DESC);
            }

            return 1;
        }).then(claim()).then(edit()).then(reload()).then(reward()).then(submit()).then(view()).then(viewAll()), Commands.SS_CMD);
    }

    private static LiteralArgumentBuilder<CommandSender> submit() {
        return literal(Commands.SUBMIT_NAME).requires(sender -> sender instanceof Player && sender.hasPermission(Permissions.SUBMIT)).then(argument(Commands.BUILD, new BuildArgumentType()).executes(context -> {
            Player player = (Player) context.getSource();
            Build build = (Build) context.getArgument(Commands.BUILD, Map.class).get(player.getUniqueId());
            if (build == null) {
                player.sendMessage(ChatColor.RED + Messages.BUILD_DOES_NOT_EXIST);
                return 0;
            }
            build.setSubmitted(!build.submitted());
            player.sendMessage(ChatColor.GREEN + Messages.PREFIX + " Build " + (build.submitted() ? "has been submitted." : " is no longer submitted."));
            return 1;
        }));
    }

    private static LiteralArgumentBuilder<CommandSender> reload() {
        return literal(Commands.RELOAD_NAME).requires(sender -> sender.hasPermission(Permissions.ADMIN)).executes(context -> {
            getSubmissions().save();
            getSubmissions().load();
            getPlugin().getPluginConfig().reload();
            context.getSource().sendMessage(ChatColor.GOLD + Messages.PLUGIN_RELOADED);
            return 1;
        });
    }

    private static LiteralArgumentBuilder<CommandSender> reward() {
        return literal(Commands.REWARD_NAME).requires(sender -> sender.hasPermission(Permissions.FEATURE)).then(argument(Commands.PLAYER, new OfflinePlayerArgument()).executes(context -> {
            OfflinePlayer offlinePlayer = context.getArgument(Commands.PLAYER, OfflinePlayer.class);
            getRewardHandler().giveReward(offlinePlayer);
            context.getSource().sendMessage(ChatColor.GOLD + Messages.rewardsGiven(offlinePlayer.getName()));
            Player player = offlinePlayer.getPlayer();
            if (player != null) {
                player.sendMessage(Component.text(Messages.REWARDS_WAITING).color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand("/ss claim")));
            }

            return 1;
        }));
    }

    private static LiteralArgumentBuilder<CommandSender> view() {
        return literal(Commands.VIEW_NAME).requires(sender -> sender instanceof Player && sender.hasPermission(Permissions.VIEW)).executes(context -> {
            new SubmittersGUI((Player) context.getSource());
            return 1;
        }).then(argument(Commands.PLAYER, new SubmitterArgumentType()).executes(context -> {
            Submitter submitter = context.getArgument(Commands.PLAYER, Submitter.class);
            new SubmitterGUI(submitter, (Player) context.getSource());
            return 1;
        }).then(argument(Commands.BUILD, new BuildFromSubmitterArgumentType()).executes(context -> {
            Player player = (Player) context.getSource();
            Submitter submitter = context.getArgument(Commands.PLAYER, Submitter.class);
            Build build = context.getArgument(Commands.BUILD, Build.class);
            if (build == null) {
                player.sendMessage(ChatColor.RED + Messages.BUILD_DOES_NOT_EXIST);
                return 0;
            }

            if (player.getUniqueId().equals(submitter.getUUID())) {
                new EditBuildGUI(build, submitter, player);
            }
            else {
                new ViewBuildGUI(build, submitter, player);
            }

            return 1;
        })));
    }

    private static LiteralArgumentBuilder<CommandSender> viewAll() {
        return literal(Commands.VIEW_ALL_NAME).requires(sender -> sender instanceof Player && sender.hasPermission(Permissions.FEATURE)).executes(context -> {
            new AllSubmissionsGUI((Player) context.getSource());
            return 1;
        });
    }
}
