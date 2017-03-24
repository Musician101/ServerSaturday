package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.uuid.UUIDCache;
import com.campmongoose.serversaturday.sponge.command.SpongeCommands;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.google.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent.Login;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;

@Plugin(id = Reference.ID, name = Reference.NAME, description = Reference.DESCRIPTION, version = Reference.VERSION)
public class SpongeServerSaturday {

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private File defaultConfig;

    @Inject
    private Logger logger;

    @Inject
    private static SpongeServerSaturday instance;

    private SpongeConfig config;
    private SpongeDescriptionChangeHandler dch;
    private SpongeSubmissions submissions;
    private UUIDCache uuidCache;

    public Logger getLogger() {
        return logger;
    }

    public static SpongeServerSaturday instance() {
        return instance;
    }

    public SpongeDescriptionChangeHandler getDescriptionChangeHandler() {
        return dch;
    }

    public SpongeSubmissions getSubmissions() {
        return submissions;
    }

    public UUIDCache getUUIDCache() {
        return uuidCache;
    }

    @Listener
    public void onDisable(GamePostInitializationEvent event) {
        logger.info(Messages.SAVING_SUBMISSIONS);
        submissions.save();
        logger.info(Messages.SUBMISSIONS_SAVED);
    }

    @Listener
    public void onEnable(GamePreInitializationEvent event) {
        logger.info(Messages.LOADING_CONFIG);
        config = new SpongeConfig(configManager, defaultConfig);
        logger.info(Messages.CONFIG_LOADED);
        logger.info(Messages.REGISTERING_UUIDS);
        uuidCache = new UUIDCache();
        Sponge.getServiceManager().getRegistration(UserStorageService.class).ifPresent(providerRegistration -> {
            UserStorageService userStorage = providerRegistration.getProvider();
            userStorage.getAll().forEach(gameProfile -> {
                UUID uuid = gameProfile.getUniqueId();
                try {
                    uuidCache.addOffline(uuid);
                }
                catch (IOException e) {
                    String name = gameProfile.getName().orElse("");
                    logger.info(Messages.uuidRegistrationFailed(name, uuid));
                    uuidCache.add(gameProfile.getUniqueId(), name);
                }
            });
        });
        Sponge.getServer().getOnlinePlayers().forEach(player -> uuidCache.add(player.getUniqueId(), player.getName()));
        logger.info(Messages.UUIDS_REGISTERED);
        logger.info(Messages.LOADING_SUBMISSIONS);
        submissions = new SpongeSubmissions(new File(defaultConfig.getParentFile(), "submitters"));
        logger.info(Messages.SUBMISSIONS_LOADED);
        dch = new SpongeDescriptionChangeHandler();
        Sponge.getEventManager().registerListener(this, Login.class, e -> {
            GameProfile profile = e.getProfile();
            UUID uuid = profile.getUniqueId();
            Optional<String> name = profile.getName();
            if (name.isPresent()) {
                uuidCache.add(uuid, name.get());
            }
            else {
                logger.warn("Could not add user to UUID Cache with id " + uuid.toString());
            }
        });
        SpongeCommands.init();
    }
    
    public SpongeConfig getConfig() {
        return config;
    }
}
