package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.sponge.command.SpongeCommands;
import com.campmongoose.serversaturday.sponge.data.manipulator.builder.UUIDDataBuilder;
import com.campmongoose.serversaturday.sponge.data.manipulator.immutable.ImmutableUUIDData;
import com.campmongoose.serversaturday.sponge.data.manipulator.mutable.UUIDData;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.textinput.SpongeTextInput;
import com.google.inject.Inject;
import java.io.File;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(id = Reference.ID, name = Reference.NAME, description = Reference.DESCRIPTION, version = Reference.VERSION)
public class SpongeServerSaturday implements ServerSaturday<SpongeRewardGiver, SpongeSubmissions> {

    private SpongeConfig config;
    @Inject
    @DefaultConfig(sharedRoot = false)
    private File defaultConfig;
    @Inject
    private Logger logger;
    @Inject
    private PluginContainer pluginContainer;
    private SpongeRewardGiver rewardGiver;
    private SpongeSubmissions submissions;

    public static SpongeServerSaturday instance() {
        return Sponge.getPluginManager().getPlugin(Reference.ID).flatMap(PluginContainer::getInstance).filter(SpongeServerSaturday.class::isInstance).map(SpongeServerSaturday.class::cast).orElseThrow(() -> new IllegalStateException("ServerSaturday is not enabled!"));
    }

    public SpongeConfig getConfig() {
        return config;
    }

    @Nonnull
    @Override
    public String getId() {
        return pluginContainer.getId();
    }

    @Nonnull
    public Logger getLogger() {
        return logger;
    }

    @Nonnull
    @Override
    public SpongeRewardGiver getRewardGiver() {
        return rewardGiver;
    }

    @Nonnull
    @Override
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
        logger.info(Messages.LOADING_CONFIG);
        config = new SpongeConfig(defaultConfig);
        logger.info(Messages.CONFIG_LOADED);
        DataRegistration.builder().name("BookGUIData").id("book_gui").dataClass(UUIDData.class).immutableClass(ImmutableUUIDData.class).builder(new UUIDDataBuilder()).build();
        logger.info(Messages.LOADING_SUBMISSIONS);
        submissions = new SpongeSubmissions(defaultConfig.getParentFile());
        Sponge.getEventManager().registerListeners(this, new SpongeTextInput());
        logger.info(Messages.SUBMISSIONS_LOADED);
        rewardGiver = new SpongeRewardGiver(new File(defaultConfig.getParentFile(), "rewards.conf"));
        SpongeCommands.init(this);
        Sponge.getEventManager().registerListener(this, ClientConnectionEvent.Join.class, e -> {
            Player player = e.getTargetEntity();
            getSubmissions().getSubmitter(player).setName(player.getName());
        });
    }
}
