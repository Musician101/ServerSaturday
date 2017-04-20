package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.command.sscommand.SSCommand;
import com.campmongoose.serversaturday.menu.RewardsMenu;
import com.campmongoose.serversaturday.submission.Submissions;
import com.campmongoose.serversaturday.util.UUIDCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerSaturday extends JavaPlugin
{
    private DescriptionChangeHandler dch;
    private RewardGiver rewardGiver;
    private Submissions submissions;
    private UUIDCache uuidCache;

    @Override
    public void onEnable()
    {
        uuidCache = new UUIDCache();
        dch = new DescriptionChangeHandler();
        rewardGiver = new RewardGiver();
        submissions = new Submissions();
        submissions.getSubmitters().forEach(submitter -> uuidCache.addIfAbsent(submitter.getUUID(), submitter.getName()));
        RewardsMenu.loadRewards();
    }

    @Override
    public void onDisable()
    {
        rewardGiver.save();
        submissions.save();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if ((command.getName().equalsIgnoreCase("serversaturday") || command.getName().equalsIgnoreCase("ss")))
            if (new SSCommand().onCommand(sender, args))
                return true;

        return false;
    }

    public DescriptionChangeHandler getDescriptionChangeHandler()
    {
        return dch;
    }

    public RewardGiver getRewardGiver() {
        return rewardGiver;
    }

    public Submissions getSubmissions()
    {
        return submissions;
    }

    public UUIDCache getUUIDCache() {
        return uuidCache;
    }

    public static ServerSaturday instance() {
        return JavaPlugin.getPlugin(ServerSaturday.class);
    }
}
