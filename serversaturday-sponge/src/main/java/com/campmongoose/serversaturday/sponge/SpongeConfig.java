package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.AbstractConfig;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import java.io.File;
import java.io.IOException;
import javax.annotation.Nonnull;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;

public class SpongeConfig extends AbstractConfig {

    public SpongeConfig(@Nonnull File configFile) {
        super(configFile);
        reload();
    }

    @Override
    public void reload() {
        SpongeServerSaturday.instance().ifPresent(plugin -> {
            ConfigurationNode config;
            Logger logger = plugin.getLogger();
            try {
                ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(configFile).build();
                if (!configFile.exists()) {
                    logger.info("Generating default config file...");
                    configFile.createNewFile();
                    config = loader.load();
                    config.getNode(Config.CONFIG_VERSION).setValue(1);
                    config.getNode(Config.MAX_BUILDS).setValue(0);
                    loader.save(config);
                    logger.info("Default file created.");
                }

                config = loader.load();
                if (config.getNode(Config.CONFIG_VERSION).isVirtual()) {
                    config.getNode(Config.CONFIG_VERSION).setValue(1);
                }

                if (config.getNode(Config.MAX_BUILDS).isVirtual()) {
                    config.getNode(Config.MAX_BUILDS).setValue(0);
                }
                maxBuilds = config.getNode(Config.MAX_BUILDS).getInt(0);
                loader.save(config);
            }
            catch (IOException e) {
                logger.info(Messages.ioException(configFile));
            }
        });
    }
}
