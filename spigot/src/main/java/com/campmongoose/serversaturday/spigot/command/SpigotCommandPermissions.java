package com.campmongoose.serversaturday.spigot.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.command.AbstractCommandPermissions;
import org.bukkit.ChatColor;

public class SpigotCommandPermissions extends AbstractCommandPermissions<String>
{
    public SpigotCommandPermissions(String permissionNode, boolean isPlayerOnly)
    {
        super(permissionNode, isPlayerOnly, ChatColor.RED + Messages.NO_PERMISSION, ChatColor.RED + Messages.PLAYER_ONLY);
    }
}
