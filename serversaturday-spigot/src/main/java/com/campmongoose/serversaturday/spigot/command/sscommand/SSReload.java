package com.campmongoose.serversaturday.spigot.command.sscommand;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.spigot.command.AbstractSpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import java.util.Collections;
import org.bukkit.ChatColor;

public class SSReload extends AbstractSpigotCommand {

    public SSReload() {
        super(Commands.RELOAD_NAME, Commands.RELOAD_DESC);
        usage = new SpigotCommandUsage(Collections.singletonList(new SpigotCommandArgument(Commands.SS_CMD + Commands.RELOAD_NAME)));
        permissions = new SpigotCommandPermissions(Permissions.RELOAD, false);
        executor = (sender, args) -> {
            try {
                getSubmissions().save();
                getSubmissions().load();
            }
            catch (SubmissionsNotLoadedException e) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
            }

            getPluginInstance().getPluginConfig().reload();
            sender.sendMessage(ChatColor.GOLD + Messages.PLUGIN_RELOADED);
            return true;
        };
    }
}
