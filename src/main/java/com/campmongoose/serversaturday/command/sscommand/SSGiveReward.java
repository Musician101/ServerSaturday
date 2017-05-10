package com.campmongoose.serversaturday.command.sscommand;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.CommandArgument.Syntax;
import com.campmongoose.serversaturday.util.UUIDCacheException;
import java.util.Arrays;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SSGiveReward extends AbstractCommand {

    public SSGiveReward() {
        super("giveReward", "Give a player a reward.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("giveReward"), new CommandArgument("player", Syntax.REQUIRED, Syntax.REPLACE)), 1, "ss.feature", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args) {
        if (!canSenderUseCommand(sender)) {
            return false;
        }

        if (!minArgsMet(sender, args.length)) {
            return false;
        }

        UUID uuid;
        try {
            uuid = getUUIDCache().getUUIDOf(args[0]);
        }
        catch (UUIDCacheException e) {
            sender.sendMessage(e.getMessage());
            return false;
        }

        getPluginInstance().getRewardGiver().addReward(uuid);
        sender.sendMessage(ChatColor.GOLD + Reference.PREFIX + "The player has received their reward.");
        return true;
    }
}
