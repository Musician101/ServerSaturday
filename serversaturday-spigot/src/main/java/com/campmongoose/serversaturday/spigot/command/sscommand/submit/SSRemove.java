package com.campmongoose.serversaturday.spigot.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.spigot.command.Syntax;
import com.campmongoose.serversaturday.spigot.gui.chest.SubmitterGUI;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSRemove extends SpigotCommand {

    public SSRemove() {
        super(Commands.REMOVE_NAME, Commands.REMOVE_DESC);
        usage = new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD + Commands.REMOVE_NAME), new SpigotCommandArgument(Commands.BUILD, Syntax.REPLACE, Syntax.REQUIRED)), 1);
        permissions = new SpigotCommandPermissions(Permissions.SUBMIT, true);
        executor = (sender, args) -> {
            Player player = (Player) sender;
            String name = StringUtils.join(args, " ");
            SpigotSubmitter submitter = getSubmitter(player);
            if (submitter.getBuild(name) == null) {
                player.sendMessage(ChatColor.RED + Messages.BUILD_NOT_FOUND);
                return false;
            }

            submitter.removeBuild(name);
            new SubmitterGUI(submitter, player);
            return true;
        };
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
        if (permissions.testPermission(sender)) {
            if (args.length >= 1) {
                SpigotSubmitter submitter = getSubmitter((Player) sender);
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i]);
                    if (args.length - 1 != i) {
                        sb.append(" ");
                    }
                }

                return submitter.getBuilds().stream().map(SpigotBuild::getName).filter(name -> name.toLowerCase().startsWith(sb.toString().toLowerCase())).collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }
}
