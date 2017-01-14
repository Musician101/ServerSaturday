package com.campmongoose.serversaturday.spigot.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.spigot.command.AbstractSpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument.Syntax;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSDescription extends AbstractSpigotCommand {

    public SSDescription() {
        super(Commands.DESCRIPTION_NAME, Commands.DESCRIPTION_DESC, new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD), new SpigotCommandArgument(Commands.DESCRIPTION_NAME), new SpigotCommandArgument(Commands.BUILD, Syntax.REQUIRED, Syntax.REPLACE)), 1), new SpigotCommandPermissions(Permissions.SUBMIT, true));
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
        if (getPluginInstance().getDescriptionChangeHandler().containsPlayer(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + Messages.EDIT_IN_PROGRESS);
            return false;
        }

        String name = combineStringArray(args);
        SpigotSubmitter submitter = getSubmitter(player);
        SpigotBuild build = submitter.getBuild(name);
        if (build == null) {
            player.sendMessage(ChatColor.RED + Messages.BUILD_NOT_FOUND);
            return false;
        }

        getPluginInstance().getDescriptionChangeHandler().add(player, build);
        return true;
    }
}
