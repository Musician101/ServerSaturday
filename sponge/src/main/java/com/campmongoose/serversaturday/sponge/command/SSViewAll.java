package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.gui.TextGUI;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class SSViewAll extends ServerSaturdayCommand {

    @Override
    public CommandResult execute(@NotNull CommandContext context) {
        TextGUI.displayAllSubmissions((ServerPlayer) context.subject(), 1);
        return CommandResult.success();
    }

    @NotNull
    @Override
    public String usage() {
        return "/ss viewAll";
    }

    @NotNull
    @Override
    public String description() {
        return "View all builds that have been submitted.";
    }

    @NotNull
    @Override
    public String name() {
        return "viewAll";
    }

    @Override
    public boolean canUse(@NotNull CommandContext context) {
        return context.hasPermission(Permissions.FEATURE) && context instanceof ServerPlayer;
    }

    @NotNull
    @Override
    public Command.Parameterized toCommand() {
        return Command.builder().executor(this).permission(Permissions.SUBMIT).shortDescription(Component.text(description())).build();
    }
}
