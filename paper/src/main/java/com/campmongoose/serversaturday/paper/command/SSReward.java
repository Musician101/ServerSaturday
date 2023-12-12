package com.campmongoose.serversaturday.paper.command;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.paper.command.argument.OfflinePlayerArgumentType;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SSReward extends ServerSaturdayCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new SSPlayer());
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/ss reward <player>";
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Give a player a reward.";
    }

    @NotNull
    @Override
    public String getName() {
        return "reward";
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender.hasPermission(Permissions.FEATURE);
    }

    static class SSPlayer extends ServerSaturdayCommand implements ArgumentCommand<OfflinePlayer> {

        @NotNull
        @Override
        public String getName() {
            return Commands.PLAYER;
        }

        @Override
        public int execute(@NotNull CommandContext<CommandSender> context) {
            OfflinePlayer offlinePlayer = context.getArgument(Commands.PLAYER, OfflinePlayer.class);
            getRewardHandler().giveReward(offlinePlayer);
            context.getSource().sendMessage(Messages.rewardsGiven(offlinePlayer.getName()));
            Player player = offlinePlayer.getPlayer();
            if (player != null) {
                player.sendMessage(Messages.REWARDS_WAITING);
            }

            return 1;
        }

        @NotNull
        @Override
        public ArgumentType<OfflinePlayer> type() {
            return new OfflinePlayerArgumentType();
        }
    }
}
