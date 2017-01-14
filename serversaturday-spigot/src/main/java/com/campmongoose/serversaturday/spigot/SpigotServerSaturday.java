package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.MySQLHandler;
import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.spigot.command.sscommand.SSCommand;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotServerSaturday extends JavaPlugin {

    private SpigotConfig config;
    private SpigotDescriptionChangeHandler dch;
    private MySQLHandler mySQLHandler;
    private SpigotSubmissions submissions;

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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return (command.getName().equalsIgnoreCase(Reference.NAME.replace(" ", "")) || command.getName().equalsIgnoreCase(Commands.SS_CMD.replace("/", ""))) && new SSCommand().onCommand(sender, args);
    }

    @Override
    public void onDisable() {
        submissions.save();
    }

    @Override
    public void onEnable() {
        config = new SpigotConfig();
        dch = new SpigotDescriptionChangeHandler();
        submissions = new SpigotSubmissions();
    }
}
