package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.AbstractConfig;
import com.campmongoose.serversaturday.common.Reference.Messages;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.ValueReference;

@ConfigSerializable
public class SpongeConfig extends AbstractConfig {

    private final ConfigurationReference<ConfigurationNode> configReference;

    public SpongeConfig(@Nonnull ConfigurationReference<ConfigurationNode> configReference) {
        this.configReference = configReference;
    }

    @Override
    public void reload() throws IOException {
        configReference.load();
        ValueReference<SpongeConfig, ConfigurationNode> config = configReference.referenceTo(SpongeConfig.class);
        SpongeConfig spongeConfig = config.get();
        if (spongeConfig != null) {
            format = spongeConfig.format;
            maxBuilds = spongeConfig.maxBuilds;
            rewards = spongeConfig.rewards;
            databaseOptions = spongeConfig.databaseOptions;
        }

        throw new IOException(Messages.CONFIG_READ_ERROR);
    }
}
