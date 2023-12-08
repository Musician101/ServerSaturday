package com.campmongoose.serversaturday.paper;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.paper.command.SSCommand;
import com.campmongoose.serversaturday.paper.submission.PaperSubmissions;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PaperServerSaturday extends JavaPlugin {

    private static class Bridge extends ServerSaturday<PaperRewardHandler, PaperSubmissions> {

        public void initRewardGiver() {
            rewardGiver = new PaperRewardHandler();
        }

        public void initSubmissions() {
            submissions = new PaperSubmissions();
        }

        @Override
        public void reloadPluginConfig() {
            getPlugin().saveDefaultConfig();
            getPlugin().reloadConfig();
            FileConfiguration fileConfiguration = getPlugin().getConfig();
            List<String> rewards = fileConfiguration.getStringList(Config.REWARDS);
            config = new com.campmongoose.serversaturday.common.Config(rewards);
        }
    }

    private final Bridge bridge = new Bridge();

    public static PaperServerSaturday getPlugin() {
        return JavaPlugin.getPlugin(PaperServerSaturday.class);
    }

    @NotNull
    public com.campmongoose.serversaturday.common.Config getPluginConfig() {
        return bridge.getPluginConfig();
    }

    @NotNull
    public PaperRewardHandler getRewardHandler() {
        return bridge.getRewardHandler();
    }

    @NotNull
    public PaperSubmissions getSubmissions() {
        return bridge.getSubmissions();
    }

    public void reloadPluginConfig() {
        bridge.reloadPluginConfig();
    }

    @Override
    public void onDisable() {
        getSubmissions().save();
        getRewardHandler().save();
    }

    @Override
    public void onEnable() {
        bridge.reloadPluginConfig();
        bridge.initSubmissions();
        getSubmissions().load();
        bridge.initRewardGiver();
        getRewardHandler().load();
        SSCommand.registerCommand();
    }
}
