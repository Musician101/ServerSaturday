package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.command.sscommand.SSCommand;
import com.campmongoose.serversaturday.menu.RewardsMenu;
import com.campmongoose.serversaturday.submission.Submissions;
import com.campmongoose.serversaturday.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.util.UUIDCache;
import com.campmongoose.serversaturday.util.UUIDCacheException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ServerSaturday extends JavaPlugin {

    private DescriptionChangeHandler dch;
    private RewardGiver rewardGiver;
    private Submissions submissions;
    private UUIDCache uuidCache;
    private int uuidCacheTaskId = -1;

    public static ServerSaturday instance() {
        return getPlugin(ServerSaturday.class);
    }

    public DescriptionChangeHandler getDescriptionChangeHandler() {
        return dch;
    }

    public RewardGiver getRewardGiver() {
        return rewardGiver;
    }

    public Submissions getSubmissions() throws SubmissionsNotLoadedException {
        if (submissions == null || !submissions.hasLoaded()) {
            throw new SubmissionsNotLoadedException();
        }

        return submissions;
    }

    public UUIDCache getUUIDCache() throws UUIDCacheException {
        if (uuidCacheTaskId != -1) {
            throw new UUIDCacheException();
        }

        return uuidCache;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((command.getName().equalsIgnoreCase("serversaturday") || command.getName().equalsIgnoreCase("ss"))) {
            if (new SSCommand().onCommand(sender, args)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onDisable() {
        rewardGiver.save();
        submissions.save();
    }

    @Override
    public void onEnable() {
        uuidCacheTaskId = new BukkitRunnable() {

            @Override
            public void run() {
                uuidCache = new UUIDCache();
                getLogger().info("Local UUID cache loaded.");
                uuidCacheTaskId = -1;
                submissions = new Submissions();
                submissions.getSubmitters().forEach(submitter -> uuidCache.addIfAbsent(submitter.getUUID(), submitter.getName()));
                getLogger().info("Submissions loaded.");
            }
        }.runTaskAsynchronously(this).getTaskId();
        dch = new DescriptionChangeHandler();
        rewardGiver = new RewardGiver();
        RewardsMenu.loadRewards();
    }
}
