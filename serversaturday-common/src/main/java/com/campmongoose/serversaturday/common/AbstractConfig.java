package com.campmongoose.serversaturday.common;

import java.io.File;
import javax.annotation.Nonnull;

public abstract class AbstractConfig {

    @Nonnull
    protected final File configFile;
    protected String saveFormat;

    protected AbstractConfig(@Nonnull File configFile) {
        this.configFile = configFile;
        reload();
    }

    @Nonnull
    public String getSaveFormat() {
        return saveFormat;
    }

    public abstract void reload();
}
