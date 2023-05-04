package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.Reference.Permissions;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;

public class SSReload extends ServerSaturdayCommand {

    @Override
    protected void addToBuilder(LiteralArgumentBuilder<CommandSender> builder) {
        builder.executes(context -> {
            getSubmissions().save();
            getSubmissions().load();
            getPlugin().getPluginConfig().reload();
            context.getSource().sendMessage(Messages.PLUGIN_RELOADED);
            return 1;
        });
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Reload the plugin.";
    }

    @Nonnull
    @Override
    public String getName() {
        return "reload";
    }

    @Nonnull
    @Override
    public String getPermission() {
        return Permissions.ADMIN;
    }
}
