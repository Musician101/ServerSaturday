package com.campmongoose.serversaturday.spigot.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.command.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractSpigotCommand extends AbstractCommand<String, SpigotCommandUsage, SpigotCommandPermissions, AbstractSpigotCommand, CommandSender>
{
    protected AbstractSpigotCommand(String name, String description, SpigotCommandUsage usage, SpigotCommandPermissions permissions)
    {
        super(name, description, usage, permissions, new ArrayList<>());
    }

    @SuppressWarnings("SameParameterValue")
    protected AbstractSpigotCommand(String name, String description, SpigotCommandUsage usage, SpigotCommandPermissions permissions, List<AbstractSpigotCommand> subCommands)
    {
        super(name, description, usage, permissions, subCommands);
    }

    @SuppressWarnings("WeakerAccess")
    public abstract boolean onCommand(CommandSender sender, String... args);

    @Override
    protected boolean minArgsMet(CommandSender sender, int args, String message)
    {
        if (args >= usage.getMinArgs())
            return true;

        sender.sendMessage(message);
        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean canSenderUseCommand(CommandSender sender)
    {
        if (permissions.isPlayerOnly() && !(sender instanceof Player))
        {
            sender.sendMessage(permissions.getNoPermission());
            return false;
        }

        if (!sender.hasPermission(permissions.getPermissionNode()))
        {
            sender.sendMessage(permissions.getPlayerOnly());
            return false;
        }

        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean minArgsMet(CommandSender sender, int args)
    {
        if (args >= usage.getMinArgs())
            return true;

        sender.sendMessage(ChatColor.RED + Messages.NOT_ENOUGH_ARGS);
        sender.sendMessage(usage.getUsage());
        return false;
    }

    String getCommandHelpInfo()
    {
        return getUsage() + " " + ChatColor.AQUA + getDescription();
    }

    protected String[] moveArguments(String[] args)
    {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, args);
        if (!list.isEmpty())
            list.remove(0);

        return list.toArray(new String[list.size()]);
    }

    protected String combineStringArray(String[] stringArray)
    {
        StringBuilder sb = new StringBuilder();
        for (String part : stringArray)
        {
            if (sb.length() > 0)
                sb.append(" ");

            sb.append(part);
        }

        return sb.toString();
    }
}
