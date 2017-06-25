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
import com.campmongoose.serversaturday.spigot.gui.chest.build.EditBuildGUI;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SSLocation extends AbstractSpigotCommand {

    public SSLocation() {
        super(Commands.LOCATION_NAME, Commands.LOCATION_DESC);
        usage = new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD + Commands.LOCATION_NAME), new SpigotCommandArgument(Commands.BUILD, Syntax.REPLACE, Syntax.REQUIRED)), 1);
        permissions = new SpigotCommandPermissions(Permissions.SUBMIT, true);
        executor = (sender, args) -> {
            Player player = (Player) sender;
            String name = StringUtils.join(args, " ");
            SpigotSubmitter submitter;
            try {
                submitter = getSubmitter(player);
            }
            catch (SSCommandException e) {
                player.sendMessage(ChatColor.RED + e.getMessage());
                return false;
            }

            if (submitter.getBuild(name) == null) {
                player.sendMessage(ChatColor.RED + Messages.BUILD_NOT_FOUND);
                return false;
            }

            SpigotBuild build = submitter.getBuild(name);
            if (build == null) {
                player.sendMessage(ChatColor.RED + Messages.BUILD_NOT_FOUND);
                return false;
            }

            build.setLocation(player.getLocation());
            new EditBuildGUI(build, submitter, player, null);
            player.sendMessage(ChatColor.GREEN + Messages.locationChanged(build));
            return true;
        };
    }
}