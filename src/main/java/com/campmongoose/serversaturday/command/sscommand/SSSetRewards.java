package com.campmongoose.serversaturday.command.sscommand;

import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.menu.RewardsMenu;
import java.util.Arrays;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSSetRewards extends AbstractCommand {

    public SSSetRewards() {
        super("setRewards", "Set the rewards for that players receive for submitting their builds.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("setRewards")), 0, "ss.feature", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args) {
        if (!canSenderUseCommand(sender)) {
            return false;
        }

        new RewardsMenu((Player) sender);
        return false;
    }
}
