package com.campmongoose.serversaturday.spigot.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.command.Syntax;
import com.campmongoose.serversaturday.common.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.command.AbstractSpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.spigot.gui.chest.BuildGUI;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.Arrays;
import net.wesjd.anvilgui.AnvilGUI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SSNew extends AbstractSpigotCommand {

    public SSNew() {
        super(Commands.NEW_NAME, Commands.NEW_DESC);
        usage = new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD + Commands.NEW_NAME), new SpigotCommandArgument(Commands.NAME, Syntax.REPLACE, Syntax.OPTIONAL)), 0);
        permissions = new SpigotCommandPermissions(Permissions.SUBMIT, true);
        executor = (sender, args) -> {
            Player player = (Player) sender;
            if (getPluginInstance().getPluginConfig().getMaxBuilds() > 0 && !player.hasPermission(Permissions.EXCEED_MAX_BUILDS)) {
                player.sendMessage(ChatColor.RED + Messages.NO_PERMISSION);
                return false;
            }

            SpigotSubmitter submitter;
            try {
                submitter = getSubmitter(player);
            }
            catch (SubmissionsNotLoadedException e) {
                player.sendMessage(ChatColor.RED + e.getMessage());
                return false;
            }

            if (args.isEmpty()) {
                new AnvilGUI(SpigotServerSaturday.instance(), player, MenuText.BUILD_DEFAULT_NAME, (p, name) -> {
                    if (submitter.getBuild(name) != null) {
                        return MenuText.ALREADY_EXISTS;
                    }

                    return null;
                });

                return true;
            }

            String name = StringUtils.join(args, " ");
            if (submitter.getBuild(name) != null) {
                player.sendMessage(ChatColor.RED + Messages.BUILD_ALREADY_EXISTS);
                return false;
            }

            SpigotBuild build = submitter.newBuild(name, player.getLocation());
            new BuildGUI(build, submitter, player, null);
            return true;
        };
    }
}
