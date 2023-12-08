package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.SpongeRewardHandler;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import static com.campmongoose.serversaturday.sponge.SpongeServerSaturday.getPlugin;

public abstract class ServerSaturdayCommand implements CommandExecutor {

    protected final SpongeRewardHandler getRewardHandler() {
        return getPlugin().getRewardHandler();
    }

    protected final SpongeSubmissions getSubmissions() {
        return getPlugin().getSubmissions();
    }

    protected final Submitter getSubmitter(ServerPlayer player) {
        return getSubmissions().getSubmitter(player.uniqueId());
    }

    protected final boolean canUseSubmit(@NotNull CommandContext context) {
        return context.subject() instanceof ServerPlayer && context.hasPermission(Permissions.SUBMIT);
    }

    @NotNull
    public abstract String description();

    @NotNull
    public abstract String name();

    @NotNull
    public abstract String usage();

    public boolean canUse(@NotNull CommandContext context) {
        return true;
    }

    @NotNull
    public abstract Command.Parameterized toCommand();
}
