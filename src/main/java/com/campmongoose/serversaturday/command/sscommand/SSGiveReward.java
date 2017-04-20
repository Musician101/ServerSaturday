package com.campmongoose.serversaturday.command.sscommand;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.CommandArgument.Syntax;
import java.util.Arrays;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        String name = args[0];
        Player player = Bukkit.getPlayer(name);
        UUID uuid;
        if (player != null) {
            uuid = player.getUniqueId();
        }
        else {
            uuid = getPluginInstance().getUUIDCache().getUUIDOf(args[0]);
        }

        getPluginInstance().getRewardGiver().addReward(uuid);
        sender.sendMessage(ChatColor.GOLD + Reference.PREFIX + "The player has received their reward.");
        return true;
    }
}
