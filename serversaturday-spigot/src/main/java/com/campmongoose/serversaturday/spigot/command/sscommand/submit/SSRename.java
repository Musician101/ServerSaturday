package com.campmongoose.serversaturday.spigot.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.command.Syntax;
import com.campmongoose.serversaturday.spigot.command.AbstractSpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.spigot.gui.anvil.NameChangeMenu;
import com.campmongoose.serversaturday.spigot.gui.chest.BuildGUI;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SSRename extends AbstractSpigotCommand {

    public SSRename() {
        super(Commands.RENAME_NAME, Commands.RENAME_DESC);
        usage = new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD + Commands.RENAME_NAME), new SpigotCommandArgument(Commands.BUILD, Syntax.REQUIRED, Syntax.REPLACE)), 1);
        permissions = new SpigotCommandPermissions(Permissions.SUBMIT, true);
        executor = (sender, args) -> {
            Player player = (Player) sender;
            String name = StringUtils.join(args, " ");
            SpigotSubmitter submitter = getSubmitter(player);
            SpigotBuild build = submitter.getBuild(name);
            if (build == null) {
                player.sendMessage(ChatColor.RED + Messages.BUILD_NOT_FOUND);
                return false;
            }

            new NameChangeMenu(build, submitter, player, new BuildGUI(build, submitter, player, null, true));
            return true;
        };
    }
}
