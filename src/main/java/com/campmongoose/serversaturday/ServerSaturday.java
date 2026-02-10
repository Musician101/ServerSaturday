package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.command.SSMain;
import com.campmongoose.serversaturday.submission.Submissions;
import io.musician101.musicommand.paper.PaperMusiCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
public final class ServerSaturday extends JavaPlugin {

    private final Messages messages = new Messages();
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
            getComponentLogger().error(Component.translatable("ss.submissions.save-failed"), e);
        }
    }

    @Override
    public void onEnable() {
        try {
            messages.load();
        }
        catch (IOException e) {
            // This is the only error that needs to be in plain English without any special formatting.
            // Makes it easier for server owners to fix or report an issue with language loading.
            getPlugin().getSLF4JLogger().error("An error occurred while loading language files.", e);
        }

        GlobalTranslator.translator().addSource(messages);
        reload();
        rewardHandler.load();
        try {
            submissions.load();
        }
        catch (IOException e) {
            getComponentLogger().error(Component.translatable("ss.submissions.load-failed"), e);
        }
        getServer().getPluginManager().registerEvents(rewardHandler, this);
        PaperMusiCommand.newAdventureInstance(this).registerCommand(new SSMain(), "ss");
    }

    public Messages messages() {
        return messages;
    }
}
