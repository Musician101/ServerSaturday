package com.campmongoose.serversaturday.spigot.command.sscommand;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.command.Syntax;
import com.campmongoose.serversaturday.common.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.common.uuid.MojangAPIException;
import com.campmongoose.serversaturday.common.uuid.PlayerNotFoundException;
import com.campmongoose.serversaturday.common.uuid.UUIDCacheException;
import com.campmongoose.serversaturday.spigot.command.AbstractSpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.spigot.gui.chest.AllSubmissionsGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.BuildGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.SubmitterGUI;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SSFeature extends AbstractSpigotCommand {

    public SSFeature() {
        super(Commands.FEATURE_NAME, Commands.FEATURE_DESC);
        usage = new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD + Commands.FEATURE_NAME), new SpigotCommandArgument(Commands.PLAYER, Syntax.OPTIONAL, Syntax.REPLACE), new SpigotCommandArgument(Commands.BUILD, Syntax.OPTIONAL, Syntax.REPLACE)));
        permissions = new SpigotCommandPermissions(Permissions.FEATURE, true);
        executor = (sender, args) -> {
            Player player = (Player) sender;
            if (!args.isEmpty()) {
                SpigotSubmitter submitter;
                try {
                    submitter = getSubmitter(args.get(0));
                }
                catch (SubmissionsNotLoadedException | UUIDCacheException | PlayerNotFoundException | MojangAPIException | IOException e) {
                    player.sendMessage(ChatColor.RED + e.getMessage());
                    return false;
                }

                if (submitter == null) {
                    player.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_FOUND);
                    return false;
                }

                if (args.size() > 1) {
                    SpigotBuild build = submitter.getBuild(StringUtils.join(moveArguments(args), " "));
                    if (build == null) {
                        player.sendMessage(ChatColor.RED + Messages.BUILD_NOT_FOUND);
                        return false;
                    }

                    build.setFeatured(!build.featured());
                    new BuildGUI(build, submitter, player, null);
                    return true;
                }

                new SubmitterGUI(player, submitter, 1, null);
                return true;
            }

            new AllSubmissionsGUI(player, 1, null);
            return true;
        };
    }
}