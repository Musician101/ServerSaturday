package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.uuid.UUIDCache;
import com.campmongoose.serversaturday.spigot.command.sscommand.SSCommand;
import com.campmongoose.serversaturday.spigot.command.sscommand.SSFeature;
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
import java.io.IOException;
import java.util.stream.Stream;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotServerSaturday extends JavaPlugin {

    private SpigotConfig config;
    private SpigotDescriptionChangeHandler dch;
    private SpigotSubmissions submissions;
    private UUIDCache uuidCache;

    public static SpigotServerSaturday instance() {
        return JavaPlugin.getPlugin(SpigotServerSaturday.class);
    }

    public SpigotDescriptionChangeHandler getDescriptionChangeHandler() {
        return dch;
    }

    public SpigotConfig getPluginConfig() {
        return config;
    }

    public SpigotSubmissions getSubmissions() {
        return submissions;
    }

    public UUIDCache getUUIDCache() {
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
        Stream.of(getServer().getOfflinePlayers()).forEach(player -> {
            try {
                uuidCache.addOffline(player.getUniqueId());
            }
            catch (IOException e) {
                getLogger().info(Messages.uuidRegistrationFailed(player.getName(), player.getUniqueId()));
                uuidCache.add(player.getUniqueId(), player.getName());
            }
        });
        getServer().getOnlinePlayers().forEach(player -> uuidCache.add(player.getUniqueId(), player.getName()));
        getLogger().info(Messages.UUIDS_REGISTERED);
        getLogger().info(Messages.LOADING_SUBMISSIONS);
        submissions = new SpigotSubmissions();
        getLogger().info(Messages.SUBMISSIONS_LOADED);
        dch = new SpigotDescriptionChangeHandler();
        Server server = getServer();
        server.getPluginManager().registerEvents(new PlayerLoginListener(), this);
        String commandPrefix = Reference.NAME.replace(" ", "").toLowerCase();
        server.getPluginCommand(commandPrefix).setExecutor(new SSCommand());
        Stream.of(new SSDescription(), new SSEdit(), new SSFeature(), new SSGoto(), new SSLocation(), new SSNew(), new SSReload(), new SSRemove(), new SSRename(), new SSResourcePack(), new SSSubmit(), new SSView(), new SSViewDescription())
                .forEach(command -> server.getPluginCommand(commandPrefix + command.getName()).setExecutor(command));
    }
}
