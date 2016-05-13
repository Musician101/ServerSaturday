package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.spigot.command.sscommand.SSCommand;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerSaturday extends JavaPlugin
{
    private SpigotDescriptionChangeHandler dch;
    private SpigotSubmissions submissions;

    @Override
    public void onEnable()
    {
        dch = new SpigotDescriptionChangeHandler();
        submissions = new SpigotSubmissions();
    }

    @Override
    public void onDisable()
    {
        submissions.save();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        return (command.getName().equalsIgnoreCase(Reference.NAME) || command.getName().equalsIgnoreCase(Commands.SS_CMD.replace("/", ""))) && new SSCommand().onCommand(sender, args);
    }

    public SpigotDescriptionChangeHandler getDescriptionChangeHandler()
    {
        return dch;
    }

    public SpigotSubmissions getSubmissions()
    {
        return submissions;
    }

    public static ServerSaturday getInstance()
    {
        return JavaPlugin.getPlugin(ServerSaturday.class);
    }
}
