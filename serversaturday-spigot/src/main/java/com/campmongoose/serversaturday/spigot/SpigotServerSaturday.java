package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.MySQLHandler;
import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.uuid.UUIDCache;
import com.campmongoose.serversaturday.spigot.command.sscommand.SSCommand;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmissions;
import java.io.IOException;
import java.util.stream.Stream;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotServerSaturday extends JavaPlugin {

    private SpigotConfig config;
    private SpigotDescriptionChangeHandler dch;
    private MySQLHandler mySQLHandler;
    private SpigotSubmissions submissions;
    private UUIDCache uuidCache;

    public static SpigotServerSaturday instance() {
        return JavaPlugin.getPlugin(SpigotServerSaturday.class);
    }

    public SpigotDescriptionChangeHandler getDescriptionChangeHandler() {
        return dch;
    }

    public MySQLHandler getMySQLHandler() {
        return mySQLHandler;
    }

    public void setMySQLHandler(MySQLHandler sql) {
        this.mySQLHandler = sql;
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return (command.getName().equalsIgnoreCase(Reference.NAME.replace(" ", "")) || command.getName().equalsIgnoreCase(Commands.SS_CMD.replace("/", ""))) && new SSCommand().onCommand(sender, args);
    }

    @Override
    public void onDisable() {
        getLogger().info("Saving submissions to disk...");
        submissions.save();
        getLogger().info("Save complete.");
    }

    @Override
    public void onEnable() {
        getLogger().info("Loading config...");
        config = new SpigotConfig();
        getLogger().info("Config loaded.");
        getLogger().info("Registering player UUIDs...");
        uuidCache = new UUIDCache();
        Stream.of(getServer().getOfflinePlayers()).forEach(player -> {
            try {
                uuidCache.addOffline(player.getUniqueId());
            }
            catch (IOException e) {
                getLogger().info("Could not retrieve up to date name for " + player.getName() + " (" + player.getUniqueId() + "). Defaulting to the last name they had on the server.");
                uuidCache.add(player.getUniqueId(), player.getName());
            }
        });
        getServer().getOnlinePlayers().forEach(player -> uuidCache.add(player.getUniqueId(), player.getName()));
        getLogger().info("Registration complete.");
        getLogger().info("Loading submissions...");
        submissions = new SpigotSubmissions();
        getLogger().info("Submissions loaded.");
        dch = new SpigotDescriptionChangeHandler();
    }
}
