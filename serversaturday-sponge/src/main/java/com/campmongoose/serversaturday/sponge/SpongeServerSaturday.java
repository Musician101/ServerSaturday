package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.command.SpongeCommands;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.google.inject.Inject;
import java.io.File;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = Reference.ID, name = Reference.NAME, description = Reference.DESCRIPTION, version = Reference.VERSION)
public class SpongeServerSaturday {

    private static SpongeServerSaturday instance;
    private SpongeConfig config;
    private SpongeDescriptionChangeHandler dch;
    @Inject
    @DefaultConfig(sharedRoot = false)
    private File defaultConfig;
    @Inject
    private Logger logger;
    private SpongeSubmissions submissions;

    public static SpongeServerSaturday instance() {
        return instance;
    }

    public SpongeConfig getConfig() {
        return config;
    }

    public SpongeDescriptionChangeHandler getDescriptionChangeHandler() {
        return dch;
    }

    public Logger getLogger() {
        return logger;
    }

    public SpongeSubmissions getSubmissions() {
        return submissions;
    }

    @Listener
    public void onDisable(GameStoppingServerEvent event) {
        logger.info(Messages.SAVING_SUBMISSIONS);
        submissions.save();
        logger.info(Messages.SUBMISSIONS_SAVED);
    }

    @Listener
    public void onEnable(GameStartingServerEvent event) {
        instance = this;
        logger.info(Messages.LOADING_CONFIG);
        config = new SpongeConfig(Sponge.getGame().getConfigManager().getPluginConfig(this));
        logger.info(Messages.CONFIG_LOADED);
        //logger.info(Messages.REGISTERING_UUIDS);
        //logger.info(Messages.UUIDS_REGISTERED);
        logger.info(Messages.LOADING_SUBMISSIONS);
        submissions = new SpongeSubmissions(defaultConfig.getParentFile());
        logger.info(Messages.SUBMISSIONS_LOADED);
        dch = new SpongeDescriptionChangeHandler();
        SpongeCommands.init();
    }
}
