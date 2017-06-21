package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.sponge.command.SpongeCommands;
import com.campmongoose.serversaturday.sponge.data.ImmutableInventorySlotData;
import com.campmongoose.serversaturday.sponge.data.InventorySlotData;
import com.campmongoose.serversaturday.sponge.data.InventorySlotDataManipulatorBuilder;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.google.inject.Inject;
import java.io.File;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = Reference.ID, name = Reference.NAME, description = Reference.DESCRIPTION, version = Reference.VERSION)
public class SpongeServerSaturday {

    private static SpongeServerSaturday instance;
    private SpongeConfig config;
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

    public Logger getLogger() {
        return logger;
    }

    public SpongeSubmissions getSubmissions() throws SubmissionsNotLoadedException {
        if (submissions == null || !submissions.hasLoaded()) {
            throw new SubmissionsNotLoadedException();
        }

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
        DataRegistration.builder().dataClass(InventorySlotData.class).immutableClass(ImmutableInventorySlotData.class)
                .manipulatorId("inventory_slot").dataName("InventorySlot")
                .builder(new InventorySlotDataManipulatorBuilder()).buildAndRegister(Sponge.getPluginManager().fromInstance(this).get());
        logger.info(Messages.LOADING_SUBMISSIONS);
        submissions = new SpongeSubmissions(defaultConfig.getParentFile());
        logger.info(Messages.SUBMISSIONS_LOADED);
        SpongeCommands.init();
    }
}
