package com.campmongoose.serversaturday.command.sscommand.view;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.CommandArgument.Syntax;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.submission.Submitter;
import com.campmongoose.serversaturday.util.UUIDCacheException;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSGoto extends AbstractCommand {

    public SSGoto() {
        super("goto", "Teleport to a build.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("goto"), new CommandArgument("player", Syntax.REQUIRED, Syntax.REPLACE), new CommandArgument("build", Syntax.REQUIRED, Syntax.REPLACE)), 2, "ss.view.goto", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args) {
        if (!canSenderUseCommand(sender)) {
            return false;
        }

        if (!minArgsMet(sender, args.length)) {
            return false;
        }

        Player player = (Player) sender;
        try {
            Submitter submitter = getSubmitter(args[0]);
            if (submitter == null) {
                player.sendMessage(ChatColor.RED + Reference.PREFIX + "Could not find a player with that name.");
                return false;
            }

            Build build = submitter.getBuild(StringUtils.join(moveArguments(args), " "));
            if (build == null) {
                player.sendMessage(ChatColor.RED + Reference.PREFIX + "A build with that name does not exist.");
                return false;
            }

            player.teleport(build.getLocation());
            player.sendMessage(ChatColor.GOLD + Reference.PREFIX + "You have teleported to " + build.getName());
            return true;
        }
        catch (SubmissionsNotLoadedException | UUIDCacheException e) {
            player.sendMessage(e.getMessage());
            return false;
        }
    }
}
