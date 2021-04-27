package com.campmongoose.serversaturday.common;

import com.campmongoose.serversaturday.common.Reference.Config;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.spongepowered.configurate.objectmapping.meta.Setting;

public abstract class AbstractConfig {

    @Nonnull
    @Setting(Config.DATABASE)
    protected Map<String, String> databaseOptions = new HashMap<>();
    @Nonnull
    @Setting
    protected String format = Config.YAML;
    @Setting(Config.MAX_BUILDS)
    protected int maxBuilds = 0;
    @Nonnull
    @Setting
    protected List<String> rewards = new ArrayList<>();

    @Nonnull
    public Map<String, String> getDatabaseOptions() {
        return databaseOptions;
    }

    @Nonnull
    public String getFormat() {
        return format;
    }

    public int getMaxBuilds() {
        return maxBuilds;
    }

    @Nonnull
    public List<String> getRewards() {
        return rewards;
    }

    public abstract void reload() throws IOException;
}
