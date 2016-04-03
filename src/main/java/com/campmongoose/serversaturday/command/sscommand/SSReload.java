package com.campmongoose.serversaturday.command.sscommand;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class SSReload extends AbstractCommand
{
    public SSReload(ServerSaturday plugin)
    {
        super(plugin, "reload", "Reload the plugin.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("reload")), 0, "ss.reload", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)
    {
        if (!canSenderUseCommand(sender))
            return false;

        plugin.getSubmissions().save(plugin);
        plugin.getSubmissions().load();
        sender.sendMessage(ChatColor.GOLD + Reference.PREFIX + "Submissions reloaded. Check console for errors.");
        return true;
    }
}
