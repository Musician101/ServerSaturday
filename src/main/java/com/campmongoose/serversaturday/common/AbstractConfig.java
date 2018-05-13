package com.campmongoose.serversaturday.common;

import java.io.File;
import javax.annotation.Nonnull;

public abstract class AbstractConfig {

    @Nonnull
    protected final File configFile;
    protected int maxBuilds;

    protected AbstractConfig(@Nonnull File configFile) {
        this.configFile = configFile;
    }

    public int getMaxBuilds() {
        return maxBuilds;
    }

    public abstract void reload();
}
