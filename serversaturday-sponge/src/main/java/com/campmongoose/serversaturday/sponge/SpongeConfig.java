package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.AbstractConfig;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
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
        SpongeServerSaturday plugin = SpongeServerSaturday.instance();
        ConfigurationNode config;
        Logger logger = plugin.getLogger();
        try {
            ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(configFile).build();
            if (!configFile.exists()) {
                logger.info("Generating default config file...");
                configFile.createNewFile();
                config = loader.load();
                config.getNode(Config.MAX_BUILDS).setValue(0);
                config.getNode(Config.REWARDS).setValue(Collections.singletonList("tell @p Quack \\_o<"));
                loader.save(config);
                logger.info("Default file created.");
            }

            config = loader.load();
            if (config.getNode(Config.MAX_BUILDS).isVirtual()) {
                config.getNode(Config.MAX_BUILDS).setValue(0);
            }

            if (config.getNode(Config.REWARDS).isVirtual()) {
                config.getNode(Config.REWARDS).setValue(Collections.singletonList("tell @p Quack \\_o<"));
            }

            maxBuilds = config.getNode(Config.MAX_BUILDS).getInt(0);
            rewards.clear();
            rewards.addAll(config.getNode(Config.REWARDS).getList(Object::toString));
            loader.save(config);
        }
        catch (IOException e) {
            logger.info(Messages.failedToReadFile(configFile));
        }
    }
}
