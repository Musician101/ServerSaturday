package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;

import static com.campmongoose.serversaturday.sponge.SpongeServerSaturday.getPlugin;

public class SSReload extends ServerSaturdayCommand {

    @Override
    public CommandResult execute(@NotNull CommandContext context) {
        getSubmissions().save();
        getSubmissions().load();
        getPlugin().reloadPluginConfig();
        context.sendMessage(Messages.PLUGIN_RELOADED);
        return CommandResult.success();
    }

    @NotNull
    @Override
    public String usage() {
        return "/ss reload";
    }

    @NotNull
    @Override
    public String description() {
        return "Reload the plugin.";
    }

    @NotNull
    @Override
    public String name() {
        return "reload";
    }

    @Override
    public boolean canUse(@NotNull CommandContext context) {
        return context.hasPermission(Permissions.ADMIN);
    }

    @NotNull
    @Override
    public Command.Parameterized toCommand() {
        return Command.builder().shortDescription(Component.text(description())).permission(Permissions.ADMIN).executor(this).build();
    }
}
