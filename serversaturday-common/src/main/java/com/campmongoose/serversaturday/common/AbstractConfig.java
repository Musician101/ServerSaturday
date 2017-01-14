package com.campmongoose.serversaturday.common;

import java.io.File;

public abstract class AbstractConfig
{
    protected final File configFile;
    protected String saveFormat;

    protected AbstractConfig(File configFile)
    {
        this.configFile = configFile;
        reload();
    }

    public String getSaveFormat()
    {
        return saveFormat;
    }

    public abstract void reload();
}
