package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.Reference.Permissions;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.LiteralCommand;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.CommandSender;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

public class SSReload extends ServerSaturdayCommand implements LiteralCommand {

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) {
        getSubmissions().save();
        getSubmissions().load();
        getPlugin().getPluginConfig().reload();
        context.getSource().sendMessage(Messages.PLUGIN_RELOADED);
        return 1;
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/ss reload";
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Reload the plugin.";
    }

    @NotNull
    @Override
    public String name() {
        return "reload";
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission(Permissions.ADMIN);
    }
}
