package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.sponge.gui.chest.AllSubmissionsGUI;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.permission.Subject;

public class SSViewAll extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandContext context) {
        Subject subject = context.subject();
        if (subject instanceof ServerPlayer) {
            new AllSubmissionsGUI((ServerPlayer) subject);
            return CommandResult.success();
        }

        return playerOnly();
    }
}
