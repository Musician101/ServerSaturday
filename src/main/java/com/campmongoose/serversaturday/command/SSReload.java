package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.Reference.Permissions;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.LiteralCommand;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

public class SSReload extends ServerSaturdayCommand implements LiteralCommand {

    @Override
    public int execute(@Nonnull CommandContext<CommandSender> context) {
        getSubmissions().save();
        getSubmissions().load();
        getPlugin().getPluginConfig().reload();
        context.getSource().sendMessage(Messages.PLUGIN_RELOADED);
        return 1;
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/ss reload";
    }

    @Nonnull
    @Override
    public String description() {
        return "Reload the plugin.";
    }

    @Nonnull
    @Override
    public String name() {
        return "reload";
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission(Permissions.ADMIN);
    }
}
