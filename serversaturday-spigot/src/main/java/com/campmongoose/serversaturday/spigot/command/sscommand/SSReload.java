package com.campmongoose.serversaturday.spigot.command.sscommand;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SSReload extends SpigotCommand {

    public SSReload() {
        super(Commands.RELOAD_NAME, Commands.RELOAD_DESC);
        usage = new SpigotCommandUsage(Collections.singletonList(new SpigotCommandArgument(Commands.SS_CMD + Commands.RELOAD_NAME)));
        permissions = new SpigotCommandPermissions(Permissions.RELOAD, false);
        executor = (sender, args) -> {
            getSubmissions().save();
            getSubmissions().load();
            getPluginInstance().getPluginConfig().reload();
            sender.sendMessage(ChatColor.GOLD + Messages.PLUGIN_RELOADED);
            return true;
        };
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
        return Collections.emptyList();
    }
}
