package com.campmongoose.serversaturday.command.sscommand.submit;

import com.campmongoose.serversaturday.DescriptionChangeHandler;
import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.CommandArgument.Syntax;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.campmongoose.serversaturday.util.UUIDCacheException;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSDescription extends AbstractCommand {

    public SSDescription() {
        super("description", "Change the description of a submission.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("description"), new CommandArgument("build", Syntax.REQUIRED, Syntax.REPLACE)), 1, "ss.submit", true);
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
        DescriptionChangeHandler dch = getPluginInstance().getDescriptionChangeHandler();
        if (dch.containsPlayer(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + Reference.PREFIX + "You're in the middle of editing another build.");
            return false;
        }

        String name = StringUtils.join(args, " ");
        try {
            Submitter submitter = getSubmitter(player);
            if (submitter.getBuild(name) == null) {
                player.sendMessage(ChatColor.RED + Reference.PREFIX + "A build with that name does not exist.");
                return false;
            }

            Build build = submitter.getBuild(name);
            dch.add(player, build);
            return true;
        }
        catch (UUIDCacheException e) {
            player.sendMessage("An error occurred while trying to complete this action.");
            return false;
        }
    }
}
