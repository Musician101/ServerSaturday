package com.campmongoose.serversaturday.command.sscommand;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.menu.RewardsMenu;
import com.campmongoose.serversaturday.util.UUIDCacheException;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SSReload extends AbstractCommand {

    public SSReload() {
        super("reload", "Reload the plugin.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("reload")), 0, "ss.reload", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args) {
        if (!canSenderUseCommand(sender)) {
            return false;
        }

        try {
            getSubmissions().save();
            getSubmissions().load();
            RewardsMenu.loadRewards();
            sender.sendMessage(ChatColor.GOLD + Reference.PREFIX + "Submissions and rewards reloaded. Check console for errors.");
            return true;
        }
        catch (UUIDCacheException e) {
            sender.sendMessage(e.getMessage());
            return false;
        }
    }
}
