package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.command.sscommand.SSCommand;
import com.campmongoose.serversaturday.menu.EditBookGUI;
import com.campmongoose.serversaturday.menu.RewardsMenu;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerSaturday extends JavaPlugin implements Listener {

    private EditBookGUI descriptionGUI;
    private EditBookGUI resourcePackGUI;
    private RewardGiver rewardGiver;
    private Submissions submissions;

    public static ServerSaturday instance() {
        return getPlugin(ServerSaturday.class);
    }

    public EditBookGUI getDescriptionGUI() {
        return descriptionGUI;
    }

    public EditBookGUI getResourcePackGUI() {
        return resourcePackGUI;
    }

    public RewardGiver getRewardGiver() {
        return rewardGiver;
    }

    public Submissions getSubmissions() {
        return submissions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return (command.getName().equalsIgnoreCase("serversaturday") || command.getName().equalsIgnoreCase("ss")) && new SSCommand().onCommand(sender, args);

    }

    @Override
    public void onDisable() {
        rewardGiver.save();
        submissions.save();
    }

    @Override
    public void onEnable() {
        submissions = new Submissions();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Submissions loaded.");
        descriptionGUI = new EditBookGUI(Build::getDescription, Build::setDescription);
        resourcePackGUI = new EditBookGUI(Build::getResourcePack, Build::setResourcePack);
        rewardGiver = new RewardGiver();
        RewardsMenu.loadRewards();
        getLogger().info("Rewards loaded.");

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        submissions.updateSubmitterName(event.getPlayer());
    }
}
