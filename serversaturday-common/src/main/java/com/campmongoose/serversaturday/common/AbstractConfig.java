package com.campmongoose.serversaturday.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public abstract class AbstractConfig {

    @Nonnull
    protected final File configFile;
    @Nonnull
    protected final List<String> rewards = new ArrayList<>();
    protected int maxBuilds;

    protected AbstractConfig(@Nonnull File configFile) {
        this.configFile = configFile;
    }

    public int getMaxBuilds() {
        return maxBuilds;
    }

    @Nonnull
    public List<String> getRewards() {
        return rewards;
    }

    public abstract void reload();
}
