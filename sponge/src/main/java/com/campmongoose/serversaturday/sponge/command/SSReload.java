package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;

import static com.campmongoose.serversaturday.sponge.SpongeServerSaturday.getPlugin;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

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
    public Component getUsage(@NotNull CommandCause cause) {
        return text("/ss reload");
    }

    @NotNull
    @Override
    public Component getDescription(@NotNull CommandCause cause) {
        return text("Reload the plugin.", GRAY);
    }

    @NotNull
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public @NotNull Optional<String> getPermission() {
        return Optional.of(Permissions.ADMIN);
    }
}
