package com.campmongoose.serversaturday.spigot.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;

public class SpigotCommandPermissions
{
    private final boolean isPlayerOnly;
    private final String permissionNode;

    public SpigotCommandPermissions(@Nonnull String permissionNode, boolean isPlayerOnly)
    {
        this.permissionNode = permissionNode;
        this.isPlayerOnly = isPlayerOnly;
    }

    public boolean isPlayerOnly()
    {
        return isPlayerOnly;
    }

    public String getNoPermission()
    {
        return ChatColor.RED + Messages.NO_PERMISSION;
    }

    public String getPermissionNode()
    {
        return permissionNode;
    }

    public String getPlayerOnly()
    {
        return ChatColor.RED + Messages.PLAYER_ONLY;
    }
}
