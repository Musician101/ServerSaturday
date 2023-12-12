package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

class SSClaim extends ServerSaturdayCommand {

    @Override
    public CommandResult execute(@NotNull CommandContext context) {
        if (context.subject() instanceof ServerPlayer player) {
            getRewardHandler().claimReward(player);
            player.sendMessage(Messages.REWARDS_RECEIVED);
            return CommandResult.success();
        }

        return CommandResult.error(Messages.PLAYER_ONLY_COMMAND);
    }

    @Override
    public @NotNull Optional<String> getPermission() {
        return Optional.of(Permissions.SUBMIT);
    }

    @NotNull
    public Component getDescription(@NotNull CommandCause cause) {
        return text("Claim any pending rewards.", GRAY);
    }

    @NotNull
    @Override
    public String getName() {
        return "claim";
    }

    @NotNull
    public Component getUsage(@NotNull CommandCause cause) {
        return text("/ss claim");
    }

    @Override
    public boolean canUse(@NotNull CommandCause cause) {
        return cause instanceof ServerPlayer;
    }
}
