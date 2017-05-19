package com.campmongoose.serversaturday.spigot.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.spigot.command.AbstractSpigotCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SSGetRewards extends AbstractSpigotCommand {

    public SSGetRewards() {
        super(Commands.GET_REWARDS_NAME, Commands.GET_REWARDS_DESC);
        executor = (sender, args) -> {
            getPluginInstance().getRewardGiver().givePlayerReward((Player) sender);
            sender.sendMessage(ChatColor.GOLD + Messages.REWARDS_GIVEN);
            return true;
        };
    }
}
