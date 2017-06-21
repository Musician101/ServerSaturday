package com.campmongoose.serversaturday.spigot.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.command.SSCommandException;
import com.campmongoose.serversaturday.spigot.command.AbstractSpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.spigot.command.Syntax;
import com.campmongoose.serversaturday.spigot.gui.chest.SubmitterGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.build.EditBuildGUI;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SSEdit extends AbstractSpigotCommand {

    public SSEdit() {
        super(Commands.EDIT_NAME, Commands.EDIT_DESC);
        usage = new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD + Commands.EDIT_NAME), new SpigotCommandArgument(Commands.BUILD, Syntax.REPLACE, Syntax.OPTIONAL)));
        permissions = new SpigotCommandPermissions(Permissions.SUBMIT, true);
        executor = (sender, args) -> {
            Player player = (Player) sender;
            SpigotSubmitter submitter;
            try {
                submitter = getSubmitter(player);
            }
            catch (SSCommandException e) {
                player.sendMessage(ChatColor.RED + e.getMessage());
                return false;
            }

            if (!args.isEmpty()) {
                String name = StringUtils.join(args, " ");
                SpigotBuild build = submitter.getBuild(name);
                if (build == null) {
                    player.sendMessage(ChatColor.RED + Messages.BUILD_NOT_FOUND);
                    return false;
                }

                new EditBuildGUI(build, submitter, player, null);
                return true;
            }

            new SubmitterGUI(player, submitter, 1, null);
            return true;
        };
    }
}
