package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.sponge.command.sscommand.SSCommand;
import com.campmongoose.serversaturday.sponge.submission.SpigotSubmissions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = Reference.ID, name = Reference.NAME, description = Reference.DESCRIPTION, version = Reference.VERSION)
public class SpongeServerSaturday
{
    private SpigotDescriptionChangeHandler dch;
    private SpigotSubmissions submissions;

    @Listener
    public void onEnable(@SuppressWarnings("UnusedParameters") GamePreInitializationEvent event)//NOSONAR
    {
        dch = new SpigotDescriptionChangeHandler();
        submissions = new SpigotSubmissions();
        Sponge.getCommandManager().register(this, new SSCommand(), Reference.NAME.replace(" ", ""));
    }

    @Listener
    public void onDisable(@SuppressWarnings("UnusedParameters") GamePostInitializationEvent event)//NOSONAR
    {
        submissions.save();
    }

    public SpigotDescriptionChangeHandler getDescriptionChangeHandler()
    {
        return dch;
    }

    public SpigotSubmissions getSubmissions()
    {
        return submissions;
    }

    public static SpongeServerSaturday instance()
    {
        //noinspection OptionalGetWithoutIsPresent
        return (SpongeServerSaturday) Sponge.getPluginManager().getPlugin(Reference.ID).get().getInstance().get();
    }
}
