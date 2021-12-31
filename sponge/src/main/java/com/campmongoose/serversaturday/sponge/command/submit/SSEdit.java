package com.campmongoose.serversaturday.sponge.command.submit;

import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.command.SSCommandExecutor;
import com.campmongoose.serversaturday.sponge.gui.chest.SubmitterGUI;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.permission.Subject;

public class SSEdit extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandContext context) {
        Subject subject = context.subject();
        if (subject instanceof ServerPlayer player) {
            Submitter<Component> submitter = getSubmitter(player);
            new SubmitterGUI(submitter, player);
            return CommandResult.success();
        }

        return playerOnly();
    }
}
