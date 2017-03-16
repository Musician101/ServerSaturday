package com.campmongoose.serversaturday.spigot.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.command.AbstractCommandPermissions;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpigotCommandPermissions extends AbstractCommandPermissions<CommandSender> {

    public SpigotCommandPermissions(@Nonnull String permissionNode, boolean isPlayerOnly) {
        super(permissionNode, isPlayerOnly, sender -> {
            if (isPlayerOnly && !(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION);
                return false;
            }

            if (!sender.hasPermission(permissionNode)) {
                sender.sendMessage(ChatColor.RED + Messages.PLAYER_ONLY);
                return false;
            }

            return true;
        });
    }
}
