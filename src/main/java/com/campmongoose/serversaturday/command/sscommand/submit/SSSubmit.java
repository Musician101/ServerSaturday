package com.campmongoose.serversaturday.command.sscommand.submit;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.CommandArgument.Syntax;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SSSubmit extends AbstractCommand
{
    public SSSubmit(ServerSaturday plugin)
    {
        super(plugin, "submit", "Toggle whether you build is ready to be featured or not.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("submit"), new CommandArgument("build", Syntax.REQUIRED, Syntax.REPLACE)), 1, "ss.submit", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)
    {
        if (!canSenderUseCommand(sender))
            return false;

        if (!minArgsMet(sender, args.length))
            return false;

        Player player = (Player) sender;
        String name = args[0];
        if (!plugin.getSubmissions().getSubmitter(player.getUniqueId()).containsBuild(name))
        {
            player.sendMessage(ChatColor.RED + Reference.PREFIX + "A build with that name does not exist.");
            return false;
        }

        //TODO submit not updating
        //TODO singular build list
        Submitter submitter = plugin.getSubmissions().getSubmitter(player.getUniqueId());
        Build build = submitter.getBuild(name);
        build.setSubmitted(!build.submitted());
        if (build.submitted())
            player.sendMessage(ChatColor.GOLD + Reference.PREFIX + "Your build has been added to the ready list.");
        else
            player.sendMessage(ChatColor.GOLD + Reference.PREFIX + "Your build has been removed from the ready list.");

        return true;
    }
}
