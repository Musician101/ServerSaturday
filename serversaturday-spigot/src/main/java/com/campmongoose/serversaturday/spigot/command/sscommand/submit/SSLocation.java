package com.campmongoose.serversaturday.spigot.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.spigot.command.AbstractSpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument.Syntax;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.spigot.menu.chest.BuildMenu;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSLocation extends AbstractSpigotCommand {

    public SSLocation() {
        super(Commands.LOCATION_NAME, Commands.LOCATION_DESC, new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD), new SpigotCommandArgument(Commands.LOCATION_NAME), new SpigotCommandArgument(Commands.BUILD, Syntax.REPLACE, Syntax.REQUIRED)), 1), new SpigotCommandPermissions(Permissions.SUBMIT, true));
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args) {
        if (!testPermission(sender)) {
            return false;
        }

        if (!minArgsMet(sender, args.length)) {
            return false;
        }

        Player player = (Player) sender;
        String name = combineStringArray(args);
        SpigotSubmitter submitter = getSubmitter(player);
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
        new BuildMenu(build, submitter, player, null);
        player.sendMessage(ChatColor.GREEN + Messages.locationChanged(build));
        return true;
    }
}