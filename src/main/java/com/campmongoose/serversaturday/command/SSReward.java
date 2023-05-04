package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.Reference.Permissions;
import com.campmongoose.serversaturday.command.argument.OfflinePlayerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import javax.annotation.Nonnull;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.musician101.bukkitier.Bukkitier.argument;

public class SSReward extends ServerSaturdayCommand {

    @Override
    protected void addToBuilder(LiteralArgumentBuilder<CommandSender> builder) {
        builder.then(argument(Commands.PLAYER, new OfflinePlayerArgumentType()).executes(context -> {
            OfflinePlayer offlinePlayer = context.getArgument(Commands.PLAYER, OfflinePlayer.class);
            getRewardHandler().giveReward(offlinePlayer);
            context.getSource().sendMessage(Messages.rewardsGiven(offlinePlayer.getName()));
            Player player = offlinePlayer.getPlayer();
            if (player != null) {
                player.sendMessage(Messages.REWARDS_WAITING);
            }

            return 1;
        }));
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Give a player a reward.";
    }

    @Nonnull
    @Override
    public String getName() {
        return "reward";
    }

    @Nonnull
    @Override
    public String getPermission() {
        return Permissions.FEATURE;
    }

    @Nonnull
    @Override
    public String getUsage() {
        return "<player>";
    }
}
