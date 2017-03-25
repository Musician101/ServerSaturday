package com.campmongoose.serversaturday.spigot.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpigotCommandPermissions {

    private final boolean isPlayerOnly;
    @Nonnull
    private final String permissionNode;

    public SpigotCommandPermissions(@Nonnull String permissionNode, boolean isPlayerOnly) {
        this.permissionNode = permissionNode;
        this.isPlayerOnly = isPlayerOnly;
    }

    @Nonnull
    public String getPermissionNode() {
        return permissionNode;
    }

    public boolean isPlayerOnly() {
        return isPlayerOnly;
    }

    public boolean testPermission(CommandSender sender) {
        if (isPlayerOnly && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION);
            return false;
        }

        if (!sender.hasPermission(permissionNode)) {
            sender.sendMessage(ChatColor.RED + Messages.PLAYER_ONLY);
            return false;
        }

        return true;
    }
}
