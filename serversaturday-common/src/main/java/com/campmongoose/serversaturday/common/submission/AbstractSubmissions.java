package com.campmongoose.serversaturday.common.submission;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractSubmissions<P, S extends AbstractSubmitter> {

    @Nonnull
    protected final File dir;
    protected final Map<UUID, S> submitters = new HashMap<>();

    protected AbstractSubmissions(@Nonnull File configDir) {
        this.dir = new File(configDir, "submitters");
        load();
    }

    @Nonnull
    public abstract S getSubmitter(@Nonnull P player);

    @Nullable
    public S getSubmitter(@Nonnull UUID uuid) {
        return submitters.get(uuid);
    }

    public List<S> getSubmitters() {
        return new ArrayList<>(submitters.values());
    }

    protected abstract void load();

    public abstract void save();
}
