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
import com.campmongoose.serversaturday.spigot.gui.book.BookGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.build.EditBuildGUI;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SSResourcePack extends AbstractSpigotCommand {

    public SSResourcePack() {
        super(Commands.RESOURCE_PACK_NAME, Commands.RESOURCE_PACK_DESC);
        usage = new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD + Commands.RESOURCE_PACK_NAME), new SpigotCommandArgument(Commands.BUILD, Syntax.REQUIRED, Syntax.REPLACE)), 1);
        permissions = new SpigotCommandPermissions(Permissions.SUBMIT, true);
        executor = (sender, args) -> {
            Player player = (Player) sender;
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                player.sendMessage(ChatColor.RED + Messages.HAND_NOT_EMPTY);
                return false;
            }

            if (BookGUI.isEditing(player)) {
                player.sendMessage(ChatColor.RED + Messages.EDIT_IN_PROGRESS);
                return false;
            }

            String name = StringUtils.join(args, " ");
            SpigotSubmitter submitter;
            try {
                submitter = getSubmitter(player);
            }
            catch (SSCommandException e) {
                player.sendMessage(ChatColor.RED + e.getMessage());
                return false;
            }

            SpigotBuild build = submitter.getBuild(name);
            if (build == null) {
                player.sendMessage(ChatColor.RED + Messages.BUILD_NOT_FOUND);
                return false;
            }

            new BookGUI(player, build, build.getResourcePack(), pages -> {
                build.setResourcePack(pages);
                new EditBuildGUI(build, submitter, player, null);
            });
            return true;
        };
    }
}
