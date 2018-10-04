package com.campmongoose.serversaturday.spigot.command.sscommand;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.gui.chest.ChestGUIs;
import com.campmongoose.serversaturday.spigot.command.SpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.spigot.command.Syntax;
import com.campmongoose.serversaturday.spigot.gui.chest.SpigotChestGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.SpigotChestGUIBuilder;
import com.campmongoose.serversaturday.spigot.gui.chest.SpigotChestGUIs;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SSFeature extends SpigotCommand {

    public SSFeature() {
        super(Commands.FEATURE_NAME, Commands.FEATURE_DESC);
        usage = new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD + Commands.FEATURE_NAME), new SpigotCommandArgument(Commands.PLAYER, Syntax.OPTIONAL, Syntax.REPLACE), new SpigotCommandArgument(Commands.BUILD, Syntax.OPTIONAL, Syntax.REPLACE)));
        permissions = new SpigotCommandPermissions(Permissions.FEATURE, true);
        executor = (sender, args) -> {
            ChestGUIs<SpigotChestGUIBuilder, ClickType, SpigotChestGUI, Inventory, SpigotBuild, Location, Player, ItemStack, String, SpigotSubmitter> guis = SpigotChestGUIs.INSTANCE;
            Player player = (Player) sender;
            if (!args.isEmpty()) {
                SpigotSubmitter submitter = getSubmitter(args.get(0));
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
                    if (submitter.getUUID().equals(player.getUniqueId())) {
                        guis.editBuild(build, submitter, player);
                    }
                    else {
                        guis.viewBuild(build, submitter, player);
                    }

                    return true;
                }


                guis.submitter(1, player, submitter);
                return true;
            }

            guis.allSubmissions(1, player);
            return true;
        };
    }
}
