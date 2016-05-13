package com.campmongoose.serversaturday.common.submission;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractSubmissions<S extends AbstractSubmitter>
{
    protected final File dir;
    protected final Map<UUID, S> submitters = new HashMap<>();

    protected AbstractSubmissions(File configDir)
    {
        this.dir = new File(configDir, "submitters");
        load();
    }

    public abstract S getSubmitter(UUID uuid);

    public List<S> getSubmitters()
    {
        return new ArrayList<>(submitters.values());
    }

    public abstract void openMenu(int page, UUID uuid);

    @SuppressWarnings("WeakerAccess")
    public abstract void load();

    public abstract void save();
}
