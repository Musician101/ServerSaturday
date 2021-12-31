package com.campmongoose.serversaturday.sponge.command.submit;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.command.SSCommandExecutor;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.permission.Subject;

public class SSGetRewards extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandContext context) {
        Subject subject = context.subject();
        if (subject instanceof ServerPlayer player) {
            getPlugin().getRewardGiver().givePlayerReward(player);
            player.sendMessage(Component.text(Messages.REWARDS_RECEIVED).color(NamedTextColor.AQUA));
            return CommandResult.success();
        }

        return playerOnly();
    }
}
