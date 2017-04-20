package com.campmongoose.serversaturday.command.sscommand.submit;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSGetRewards extends AbstractCommand {

    public SSGetRewards() {
        super("getRewards", "Receive any rewards that are currently waiting.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("getRewards")), 0, "ss.submit", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args) {
        if (!canSenderUseCommand(sender)) {
            return false;
        }

        ServerSaturday.instance().getRewardGiver().givePlayerReward((Player) sender);
        sender.sendMessage(ChatColor.GOLD + Reference.PREFIX + "All rewards have been given to you.");
        return true;
    }
}
