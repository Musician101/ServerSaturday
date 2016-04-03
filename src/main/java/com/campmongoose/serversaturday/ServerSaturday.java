package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.command.sscommand.SSCommand;
import com.campmongoose.serversaturday.submission.Submissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerSaturday extends JavaPlugin
{
    private DescriptionChangeHandler dch;
    private Submissions submissions;

    @Override
    public void onEnable()
    {
        dch = new DescriptionChangeHandler(this);
        submissions = new Submissions(this);
    }

    @Override
    public void onDisable()
    {
        submissions.save(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if ((command.getName().equalsIgnoreCase("serversaturday") || command.getName().equalsIgnoreCase("ss")))
            if (new SSCommand(this).onCommand(sender, args))
                return true;

        return false;
    }

    public DescriptionChangeHandler getDescriptionChangeHandler()
    {
        return dch;
    }

    public Submissions getSubmissions()
    {
        return submissions;
    }
}
