package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
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

public class SSMyBuilds extends ServerSaturdayCommand {

    @Override
    public CommandResult execute(@NotNull CommandContext context) {
        if (context.subject() instanceof ServerPlayer player) {
            TextGUI.displaySubmitter(player, getSubmitter(player), 1);
            return CommandResult.success();
        }

        return CommandResult.error(Messages.PLAYER_ONLY_COMMAND);
    }

    @NotNull
    @Override
    public Component getUsage(@NotNull CommandCause cause) {
        return text("/ss myBuilds");
    }

    @NotNull
    @Override
    public Component getDescription(@NotNull CommandCause cause) {
        return text("View your builds.", GRAY);
    }

    @Override
    public @NotNull Optional<String> getPermission() {
        return Optional.of(Permissions.SUBMIT);
    }

    @NotNull
    @Override
    public String getName() {
        return "myBuilds";
    }

    @Override
    public boolean canUse(@NotNull CommandCause cause) {
        return cause instanceof ServerPlayer;
    }
}
