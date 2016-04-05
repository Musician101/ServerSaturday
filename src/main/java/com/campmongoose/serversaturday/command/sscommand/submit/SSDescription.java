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

public class SSDescription extends AbstractCommand
{
    public SSDescription(ServerSaturday plugin)
    {
        super(plugin, "description", "Change the description of a submission.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("description"), new CommandArgument("build", Syntax.REQUIRED, Syntax.REPLACE)), 1, "ss.submit", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)
    {
        if (!canSenderUseCommand(sender))
            return false;

        if (!minArgsMet(sender, args.length))
            return false;

        Player player = (Player) sender;
        if (plugin.getDescriptionChangeHandler().containsPlayer(player.getUniqueId()))
        {
            player.sendMessage(ChatColor.RED + Reference.PREFIX + "You're in the middle of editing another build.");
            return false;
        }

        String name = combineStringArray(args);
        Submitter submitter = plugin.getSubmissions().getSubmitter(player.getUniqueId());
        if (submitter.getBuild(name) == null)
        {
            player.sendMessage(ChatColor.RED + Reference.PREFIX + "A build with that name does not exist.");
            return false;
        }

        Build build = submitter.getBuild(name);
        plugin.getDescriptionChangeHandler().add(player, build);
        return true;
    }
}
