package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

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

    @NotNull
    @Override
    public String description() {
        return "Claim any pending rewards.";
    }

    @NotNull
    @Override
    public String name() {
        return "claim";
    }

    @NotNull
    @Override
    public String usage() {
        return "/ss claim";
    }

    @Override
    public boolean canUse(@NotNull CommandContext context) {
        return canUseSubmit(context);
    }

    @NotNull
    @Override
    public Command.Parameterized toCommand() {
        return Command.builder().permission(Permissions.SUBMIT).executor(this).shortDescription(Component.text(description())).build();
    }
}
