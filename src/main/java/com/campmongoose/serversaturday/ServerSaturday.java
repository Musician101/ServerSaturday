package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.command.SSCommand;
import com.campmongoose.serversaturday.submission.Submissions;
import javax.annotation.Nonnull;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerSaturday extends JavaPlugin {

    @Nonnull
    private final PluginConfig config = new PluginConfig();
    @Nonnull
    private final RewardHandler rewardHandler = new RewardHandler();
    @Nonnull
    private final Submissions submissions = new Submissions();

    @Nonnull
    public static ServerSaturday getInstance() {
        return getPlugin(ServerSaturday.class);
    }

    @Nonnull
    public PluginConfig getPluginConfig() {
        return config;
    }

    @Nonnull
    public RewardHandler getRewardHandler() {
        return rewardHandler;
    }

    @Nonnull
    public Submissions getSubmissions() {
        return submissions;
    }

    @Override
    public void onDisable() {
        rewardHandler.save();
        submissions.save();
    }

    @Override
    public void onEnable() {
        config.reload();
        rewardHandler.load();
        submissions.load();
        getServer().getPluginManager().registerEvents(rewardHandler, this);
        SSCommand.registerCommand();
    }
}
