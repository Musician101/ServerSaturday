package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.gui.TextGUI;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

public class SSViewAll extends ServerSaturdayCommand {

    @Override
    public CommandResult execute(@NotNull CommandContext context) {
        TextGUI.displayAllSubmissions((ServerPlayer) context.subject(), 1);
        return CommandResult.success();
    }

    @NotNull
    @Override
    public Component getUsage(@NotNull CommandCause cause) {
        return text("/ss viewAll");
    }

    @NotNull
    @Override
    public Component getDescription(@NotNull CommandCause cause) {
        return text("View all builds that have been submitted.", GRAY);
    }

    @NotNull
    @Override
    public String getName() {
        return "viewAll";
    }

    @Override
    public boolean canUse(@NotNull CommandCause cause) {
        return cause instanceof ServerPlayer;
    }

    @Override
    public @NotNull Optional<String> getPermission() {
        return Optional.of(Permissions.FEATURE);
    }
}
