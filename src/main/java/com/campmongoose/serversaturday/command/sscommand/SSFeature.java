package com.campmongoose.serversaturday.command.sscommand;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.CommandArgument.Syntax;
import com.campmongoose.serversaturday.menu.chest.AllSubmissionsMenu;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.campmongoose.serversaturday.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;

public class SSFeature extends AbstractCommand
{
    public SSFeature(ServerSaturday plugin)
    {
        super(plugin, "feature", "Toggle if a build has been featured on Server Saturday.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("feature"), new CommandArgument("player", Syntax.OPTIONAL, Syntax.REPLACE), new CommandArgument("build", Syntax.OPTIONAL, Syntax.REPLACE)), 0, "ss.feature", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)
    {
        if (!canSenderUseCommand(sender))
            return false;

        Player player = (Player) sender;
        if (args.length > 0)
        {
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

            if (args.length > 1)
            {
                Build build = submitter.getBuild(combineStringArray(moveArguments(args)));
                if (build == null)
                {
                    player.sendMessage(ChatColor.RED + Reference.PREFIX + "A build with that name does not exist.");
                    return false;
                }

                build.setFeatured(!build.featured());
                build.openMenu(plugin, submitter, player);
                return true;
            }

            submitter.openMenu(plugin, 1, player);
            return true;
        }

        new AllSubmissionsMenu(plugin, 1).open(player);
        return true;
    }
}
