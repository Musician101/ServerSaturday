package com.campmongoose.serversaturday.command.sscommand;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.CommandArgument.Syntax;
import com.campmongoose.serversaturday.menu.chest.AllSubmissionsMenu;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSFeature extends AbstractCommand
{
    public SSFeature()
    {
        super("feature", "Toggle if a build has been featured on Server Saturday.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("feature"), new CommandArgument("player", Syntax.OPTIONAL, Syntax.REPLACE), new CommandArgument("build", Syntax.OPTIONAL, Syntax.REPLACE)), 0, "ss.feature", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)
    {
        if (!canSenderUseCommand(sender))
            return false;

        Player player = (Player) sender;
        if (args.length > 0)
        {
            Submitter submitter = getSubmitter(args[0]);
            if (submitter == null)
            {
                player.sendMessage(ChatColor.RED + Reference.PREFIX + "Could not find a player with that name.");
                return false;
            }

            if (args.length > 1)
            {
                Build build = submitter.getBuild(StringUtils.join(moveArguments(args), " "));
                if (build == null)
                {
                    player.sendMessage(ChatColor.RED + Reference.PREFIX + "A build with that name does not exist.");
                    return false;
                }

                build.setFeatured(!build.featured());
                getPluginInstance().getRewardGiver().addReward(submitter.getUUID());
                build.openMenu(submitter, player);
                return true;
            }

            submitter.openMenu(1, player);
            return true;
        }

        new AllSubmissionsMenu(1).open(player);
        return true;
    }
}
