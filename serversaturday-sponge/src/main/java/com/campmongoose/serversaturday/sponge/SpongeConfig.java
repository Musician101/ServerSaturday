package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.AbstractConfig;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import java.io.IOException;
import javax.annotation.Nonnull;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigRoot;

public class SpongeConfig extends AbstractConfig {

    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    public SpongeConfig(@Nonnull ConfigRoot configRoot) {
        super(configRoot.getConfigPath().toFile());
        this.configManager = configRoot.getConfig();
        reload();
    }

    @Override
    public void reload() {
        ConfigurationNode config;
        SpongeServerSaturday plugin = SpongeServerSaturday.instance();
        Logger logger = plugin.getLogger();
        try {
            if (!configFile.exists()) {
                logger.info("Generating default config file...");
                configFile.createNewFile();
                config = configManager.load();
                config.getNode(Config.CONFIG_VERSION).setValue(1);
                config.getNode(Config.MAX_BUILDS).setValue(10);
                configManager.save(config);
                logger.info("Default file created.");
            }

            //TODO still throwing NPE
            config = configManager.load();
        }
        catch (IOException e) {
            logger.info(Messages.ioException(configFile));
            return;
        }

        //Config update code goes here when ready

        maxBuilds = config.getNode(Config.MAX_BUILDS).getInt(10);
    }
}
