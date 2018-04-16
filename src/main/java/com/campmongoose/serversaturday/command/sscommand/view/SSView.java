package com.campmongoose.serversaturday.command.sscommand.view;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.CommandArgument.Syntax;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submissions;
import com.campmongoose.serversaturday.submission.Submitter;
import com.campmongoose.serversaturday.util.MojangAPIException;
import com.campmongoose.serversaturday.util.PlayerNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSView extends AbstractCommand {

    public SSView() {
        super("view", "View Server Saturday submissions.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("view"), new CommandArgument("player", Syntax.OPTIONAL, Syntax.REPLACE), new CommandArgument("build", Syntax.OPTIONAL, Syntax.REPLACE)), 0, "ss.view", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args) {
        if (!canSenderUseCommand(sender)) {
            return false;
        }

        Player player = (Player) sender;
        try {
            Submissions submissions = getSubmissions();
            if (args.length > 0) {
                Submitter submitter = getSubmitter(args[0]);
                if (submitter == null) {
                    player.sendMessage(ChatColor.RED + Reference.PREFIX + "Could not find a player with that name.");
                    return false;
                }

                if (args.length > 1) {
                    Build build = submitter.getBuild(StringUtils.join(moveArguments(args), " "));
                    if (build == null) {
                        player.sendMessage(ChatColor.RED + Reference.PREFIX + "A build with that name does not exist.");
                        return false;
                    }

                    build.openMenu(submitter, player);
                    return true;
                }

                submitter.openMenu(1, player);
            }
            else {
                submissions.openMenu(1, player);
            }
        }
        catch (PlayerNotFoundException | MojangAPIException | IOException e) {
            player.sendMessage(e.getMessage());
            return false;
        }

        return true;
    }
}
