package com.campmongoose.serversaturday.common;

import com.campmongoose.serversaturday.common.submission.Submissions;
import java.io.IOException;
import java.net.URISyntaxException;
import org.jetbrains.annotations.NotNull;

public abstract class ServerSaturday<R extends RewardHandler<?>, S extends Submissions> {

    @NotNull
    protected Config config = new Config();
    protected R rewardGiver;
    protected S submissions;

    @NotNull
    public Config getPluginConfig() {
        return config;
    }

    @NotNull
    public R getRewardHandler() {
        return rewardGiver;
    }

    @NotNull
    public S getSubmissions() {
        return submissions;
    }

    public abstract void reloadPluginConfig() throws IOException, URISyntaxException;
}
