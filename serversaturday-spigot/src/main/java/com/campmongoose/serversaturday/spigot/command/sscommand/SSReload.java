package com.campmongoose.serversaturday.spigot.command.sscommand;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.spigot.command.AbstractSpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SSReload extends AbstractSpigotCommand {

    public SSReload() {
        super(Commands.RELOAD_NAME, Commands.RELOAD_DESC, new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD), new SpigotCommandArgument(Commands.RELOAD_NAME))), new SpigotCommandPermissions(Permissions.RELOAD, false));
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args) {
        if (!testPermission(sender)) {
            return false;
        }

        getSubmissions().save();
        getSubmissions().load();
        sender.sendMessage(ChatColor.GOLD + Messages.PLUGIN_RELOADED);
        return true;
    }
}