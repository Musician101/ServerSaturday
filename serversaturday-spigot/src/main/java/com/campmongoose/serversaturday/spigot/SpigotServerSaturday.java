package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.common.uuid.UUIDCache;
import com.campmongoose.serversaturday.common.uuid.UUIDCacheException;
import com.campmongoose.serversaturday.spigot.command.sscommand.SSCommand;
import com.campmongoose.serversaturday.spigot.command.sscommand.SSFeature;
import com.campmongoose.serversaturday.spigot.command.sscommand.SSGiveReward;
import com.campmongoose.serversaturday.spigot.command.sscommand.SSReload;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSDescription;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSEdit;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSLocation;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSNew;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSRemove;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSRename;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSResourcePack;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSSubmit;
import com.campmongoose.serversaturday.spigot.command.sscommand.view.SSGoto;
import com.campmongoose.serversaturday.spigot.command.sscommand.view.SSView;
import com.campmongoose.serversaturday.spigot.command.sscommand.view.SSViewDescription;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmissions;
import java.util.stream.Stream;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SpigotServerSaturday extends JavaPlugin {

    private SpigotConfig config;
    private SpigotRewardGiver rewardGiver;
    private SpigotSubmissions submissions;
    private UUIDCache uuidCache;
    private int taskId = -1;

    public static SpigotServerSaturday instance() {
        return JavaPlugin.getPlugin(SpigotServerSaturday.class);
    }

    public SpigotConfig getPluginConfig() {
        return config;
    }

    public SpigotRewardGiver getRewardGiver() {
        return rewardGiver;
    }

    public SpigotSubmissions getSubmissions() throws SubmissionsNotLoadedException {
        if (submissions == null || !submissions.hasLoaded()) {
            throw new SubmissionsNotLoadedException();
        }

        return submissions;
    }

    public UUIDCache getUUIDCache() throws UUIDCacheException {
        if (taskId != -1) {
            throw new UUIDCacheException();
        }

        return uuidCache;
    }

    @Override
    public void onDisable() {
        getLogger().info(Messages.SAVING_SUBMISSIONS);
        submissions.save();
        getLogger().info(Messages.SUBMISSIONS_SAVED);
    }

    @Override
    public void onEnable() {
        getLogger().info(Messages.LOADING_CONFIG);
        config = new SpigotConfig();
        getLogger().info(Messages.CONFIG_LOADED);
        getLogger().info(Messages.REGISTERING_UUIDS);
        uuidCache = new UUIDCache();
        taskId = new BukkitRunnable() {

            @Override
            public void run() {
                Stream.of(getServer().getOfflinePlayers()).forEach(player -> uuidCache.add(player.getUniqueId(), player.getName()));
                getServer().getOnlinePlayers().forEach(player -> uuidCache.add(player.getUniqueId(), player.getName()));
                getLogger().info(Messages.UUIDS_REGISTERED);
                taskId = -1;
                getLogger().info(Messages.LOADING_SUBMISSIONS);
                submissions = new SpigotSubmissions();
                submissions.getSubmitters().forEach(spigotSubmitter -> uuidCache.addIfAbsent(spigotSubmitter.getUUID(), spigotSubmitter.getName()));
                getLogger().info(Messages.SUBMISSIONS_LOADED);
            }
        }.runTaskAsynchronously(SpigotServerSaturday.instance()).getTaskId();
        rewardGiver = new SpigotRewardGiver();
        Server server = getServer();
        server.getPluginManager().registerEvents(new PlayerLoginListener(), this);
        String commandPrefix = Reference.NAME.replace(" ", "").toLowerCase();
        server.getPluginCommand(commandPrefix).setExecutor(new SSCommand());
        Stream.of(new SSDescription(), new SSEdit(), new SSFeature(), new SSGiveReward(), new SSGoto(), new SSLocation(), new SSNew(), new SSReload(), new SSRemove(), new SSRename(), new SSResourcePack(), new SSSubmit(), new SSView(), new SSViewDescription())
                .forEach(command -> server.getPluginCommand(commandPrefix + command.getName()).setExecutor(command));
    }
}
