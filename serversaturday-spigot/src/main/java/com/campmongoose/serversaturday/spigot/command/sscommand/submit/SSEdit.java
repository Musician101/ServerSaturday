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
import com.campmongoose.serversaturday.spigot.menu.chest.SubmitterMenu;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSEdit extends AbstractSpigotCommand {

    public SSEdit() {
        super(Commands.EDIT_NAME, Commands.EDIT_DESC, new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD), new SpigotCommandArgument(Commands.EDIT_NAME), new SpigotCommandArgument(Commands.BUILD, Syntax.REPLACE, Syntax.OPTIONAL))), new SpigotCommandPermissions(Permissions.SUBMIT, true));
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args) {
        if (!testPermission(sender)) {
            return false;
        }

        Player player = (Player) sender;
        SpigotSubmitter submitter = getSubmitter(player);
        if (args.length > 0) {
            String name = combineStringArray(args);
            SpigotBuild build = submitter.getBuild(name);
            if (build == null) {
                player.sendMessage(ChatColor.RED + Messages.BUILD_NOT_FOUND);
                return false;
            }

            new BuildMenu(build, submitter, player, null);
            return true;
        }

        new SubmitterMenu(player, submitter, 1, null);
        return true;
    }
}
