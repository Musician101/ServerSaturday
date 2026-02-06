package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.command.SSMain;
import com.campmongoose.serversaturday.submission.Submissions;
import io.musician101.musicommand.paper.PaperMusiCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
public final class ServerSaturday extends JavaPlugin {

    private final RewardHandler rewardHandler = new RewardHandler();
    private final Submissions submissions = new Submissions();

    public static ServerSaturday getPlugin() {
        return getPlugin(ServerSaturday.class);
    }

    public RewardHandler getRewardHandler() {
        return rewardHandler;
    }

    public Submissions getSubmissions() {
        return submissions;
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();
    }

    @Override
    public void onDisable() {
        rewardHandler.save();
        try {
            submissions.save();
        }
        catch (IOException e) {
            getSLF4JLogger().error("An error occurred while loading submissions.", e);
        }
    }

    @Override
    public void onEnable() {
        reload();
        rewardHandler.load();
        try {
            submissions.load();
        }
        catch (IOException e) {
            getSLF4JLogger().error("An error occurred while saving submissions.", e);
        }
        getServer().getPluginManager().registerEvents(rewardHandler, this);
        PaperMusiCommand.newAdventureInstance(this).registerCommand(new SSMain(), "ss");
    }
}
