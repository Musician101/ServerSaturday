package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.AbstractConfig;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import java.io.File;
import java.io.IOException;
import javax.annotation.Nonnull;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;

public class SpongeConfig extends AbstractConfig {

    private ConfigurationLoader<CommentedConfigurationNode> configManager;
    private ConfigurationNode config;

    public SpongeConfig(@Nonnull ConfigurationLoader<CommentedConfigurationNode> configManager, @Nonnull File configFile) {
        super(configFile);
        this.configManager = configManager;
    }

    @Override
    public void reload() {
        SpongeServerSaturday plugin = SpongeServerSaturday.instance();
        Logger logger = plugin.getLogger();
        try {
            if (!configFile.exists()) {
                logger.info("Generating default config file...");
                configFile.createNewFile();
                config = configManager.load();
                config.getNode("config_version").setValue(1);
                config.getNode(Config.MAX_BUILDS).setValue(10);
                configManager.save(config);
                logger.info("Default file created.");
            }

            config = configManager.load();
        }
        catch (IOException e) {
            logger.info(Messages.ioException(configFile));
        }

        //Config update code goes here when ready

        maxBuilds = config.getNode(Config.MAX_BUILDS).getInt(10);
    }
}
