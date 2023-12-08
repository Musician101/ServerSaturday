package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.gui.TextGUI;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

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
    public String usage() {
        return "/ss myBuilds";
    }

    @NotNull
    @Override
    public String description() {
        return "View your builds.";
    }

    @NotNull
    @Override
    public String name() {
        return "myBuilds";
    }

    @Override
    public boolean canUse(@NotNull CommandContext context) {
        return canUseSubmit(context);
    }

    @NotNull
    @Override
    public Command.Parameterized toCommand() {
        return Command.builder().permission(Permissions.SUBMIT).shortDescription(Component.text(description())).executor(this).build();
    }
}
