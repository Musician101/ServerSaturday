package com.campmongoose.serversaturday.command.sscommand.submit;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.CommandArgument.Syntax;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSLocation extends AbstractCommand {

    public SSLocation() {
        super("location", "Change the location of a build.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("location"), new CommandArgument("build", Syntax.REPLACE, Syntax.REQUIRED)), 1, "ss.submit", true);
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
        String name = StringUtils.join(args, " ");
        Submitter submitter = getSubmitter(player);
        if (submitter.getBuild(name) == null) {
            player.sendMessage(ChatColor.RED + Reference.PREFIX + "That build does not exist.");
            return false;
        }

        Build build = submitter.getBuild(name);
        build.setLocation(player.getLocation());
        build.openMenu(submitter, player);
        return true;
    }
}
