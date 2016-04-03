package com.campmongoose.serversaturday.command.sscommand.view;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.CommandArgument.Syntax;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.campmongoose.serversaturday.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;

public class SSGoto extends AbstractCommand
{
    public SSGoto(ServerSaturday plugin)
    {
        super(plugin, "goto", "Teleport to a build.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("goto"), new CommandArgument("player", Syntax.REQUIRED, Syntax.REPLACE), new CommandArgument("build", Syntax.REQUIRED, Syntax.REPLACE)), 2, "ss.view.goto", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)
    {
        if (!canSenderUseCommand(sender))
            return false;

        if (!minArgsMet(sender, args.length))
            return false;

        Player player = (Player) sender;
        Submitter submitter = null;
        try
        {
            submitter = plugin.getSubmissions().getSubmitter(UUIDUtils.getUUIDOf(args[0]));
        }
        catch (IOException e)
        {
            for (Submitter s : plugin.getSubmissions().getSubmitters())
                if (s.getName().equalsIgnoreCase(args[0]))
                    submitter = s;
        }

        if (submitter == null)
        {
            player.sendMessage(ChatColor.RED + Reference.PREFIX + "Could not find a player with that name.");
            return false;
        }

        Build build = submitter.getBuild(args[1]);
        if (build == null)
        {
            player.sendMessage(ChatColor.RED + Reference.PREFIX + "A build with that name does not exist.");
            return false;
        }

        player.teleport(build.getLocation());
        player.sendMessage(ChatColor.GOLD + Reference.PREFIX + "You have teleported to " + build.getName());
        return true;
    }
}
