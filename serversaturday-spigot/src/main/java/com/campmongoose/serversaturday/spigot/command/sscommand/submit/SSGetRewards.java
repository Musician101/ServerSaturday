package com.campmongoose.serversaturday.spigot.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSGetRewards extends SpigotCommand {

    public SSGetRewards() {
        super(Commands.GET_REWARDS_NAME, Commands.GET_REWARDS_DESC);
        usage = new SpigotCommandUsage(Collections.singletonList(new SpigotCommandArgument(Commands.SS_CMD + Commands.GET_REWARDS_NAME)));
        permissions = new SpigotCommandPermissions(Permissions.VIEW, false);
        executor = (sender, args) -> {
            getPluginInstance().getRewardGiver().givePlayerReward((Player) sender);
            sender.sendMessage(ChatColor.GOLD + Messages.REWARDS_RECEIVED);
            return true;
        };
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
        return Collections.emptyList();
    }
}
