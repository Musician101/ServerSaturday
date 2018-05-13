package com.campmongoose.serversaturday.spigot.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import java.util.Collections;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SSGetRewards extends SpigotCommand {

    public SSGetRewards() {
        super(Commands.GET_REWARDS_NAME, Commands.GET_REWARDS_DESC);
        usage = new SpigotCommandUsage(Collections.singletonList(new SpigotCommandArgument(Commands.SS_CMD + Commands.GOTO_NAME)));
        permissions = new SpigotCommandPermissions(Permissions.VIEW_GOTO, false);
        executor = (sender, args) -> {
            getPluginInstance().getRewardGiver().givePlayerReward((Player) sender);
            sender.sendMessage(ChatColor.GOLD + Messages.REWARDS_GIVEN);
            return true;
        };
    }
}
