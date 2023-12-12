package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.SpongeRewardHandler;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import io.musician101.spongecmd.CMDExecutor;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import static com.campmongoose.serversaturday.sponge.SpongeServerSaturday.getPlugin;

public abstract class ServerSaturdayCommand implements CMDExecutor {

    protected final SpongeRewardHandler getRewardHandler() {
        return getPlugin().getRewardHandler();
    }

    protected final SpongeSubmissions getSubmissions() {
        return getPlugin().getSubmissions();
    }

    protected final Submitter getSubmitter(ServerPlayer player) {
        return getSubmissions().getSubmitter(player.uniqueId());
    }
}
