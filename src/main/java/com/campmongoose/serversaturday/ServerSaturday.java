package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.command.SSCommand;
import com.campmongoose.serversaturday.submission.Submissions;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class ServerSaturday extends JavaPlugin {

    @NotNull
    private final PluginConfig config = new PluginConfig();
    @NotNull
    private final RewardHandler rewardHandler = new RewardHandler();
    @NotNull
    private final Submissions submissions = new Submissions();

    @NotNull
    public static ServerSaturday getPlugin() {
        return getPlugin(ServerSaturday.class);
    }

    @NotNull
    public PluginConfig getPluginConfig() {
        return config;
    }

    @NotNull
    public RewardHandler getRewardHandler() {
        return rewardHandler;
    }

    @NotNull
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
