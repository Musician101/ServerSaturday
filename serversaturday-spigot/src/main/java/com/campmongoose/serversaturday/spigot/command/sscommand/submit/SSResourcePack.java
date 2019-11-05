package com.campmongoose.serversaturday.spigot.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.spigot.command.Syntax;
import com.campmongoose.serversaturday.spigot.gui.SpigotBookGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.SpigotChestGUIs;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SSResourcePack extends SpigotCommand {

    public SSResourcePack() {
        super(Commands.RESOURCE_PACK_NAME, Commands.RESOURCE_PACK_DESC);
        usage = new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD + Commands.RESOURCE_PACK_NAME), new SpigotCommandArgument(Commands.BUILD, Syntax.REQUIRED, Syntax.REPLACE)), 1);
        permissions = new SpigotCommandPermissions(Permissions.SUBMIT, true);
        executor = (sender, args) -> {
            Player player = (Player) sender;
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() != Material.AIR) {
                player.sendMessage(ChatColor.RED + Messages.HAND_NOT_EMPTY);
                return false;
            }

            if (SpigotBookGUI.isEditing(player)) {
                player.sendMessage(ChatColor.RED + Messages.EDIT_IN_PROGRESS);
                return false;
            }

            String name = StringUtils.join(args, " ");
            SpigotSubmitter submitter = getSubmitter(player);
            SpigotBuild build = submitter.getBuild(name);
            if (build == null) {
                player.sendMessage(ChatColor.RED + Messages.BUILD_NOT_FOUND);
                return false;
            }

            new SpigotBookGUI(player, build, build.getResourcePacks(), pages -> {
                build.setResourcePacks(pages);
                SpigotChestGUIs.INSTANCE.editBuild(build, submitter, player);
            });
            return true;
        };
    }
}
