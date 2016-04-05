package com.campmongoose.serversaturday.command.sscommand.submit;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.CommandArgument.Syntax;
import com.campmongoose.serversaturday.submission.Submitter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SSNew extends AbstractCommand
{
    public SSNew(ServerSaturday plugin)
    {
        super(plugin, "new", "Register a new build for Server Saturday.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("new"), new CommandArgument("name", Syntax.REPLACE, Syntax.REQUIRED)), 1, "ss.submit", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)
    {
        if (!canSenderUseCommand(sender))
            return false;

        if (!minArgsMet(sender, args.length))
            return false;

        Player player = (Player) sender;
        String name = combineStringArray(args);
        Submitter submitter = plugin.getSubmissions().getSubmitter(player.getUniqueId());
        if (submitter.getBuild(name) != null)
        {
            player.sendMessage(ChatColor.RED + Reference.PREFIX + "A build with that name already exists.");
            return false;
        }

        submitter.newBuild(name, player.getLocation()).openMenu(plugin, submitter, player);
        return true;
    }
}
