package com.campmongoose.serversaturday.common.submission;

import com.google.gson.Gson;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Submissions<P, S extends Submitter> {

    @Nonnull
    protected final File dir;
    protected final Gson gson;
    protected final Map<UUID, S> submitters = new HashMap<>();

    protected Submissions(@Nonnull File configDir, Gson gson) {
        this.dir = new File(configDir, "submitters");
        this.gson = gson;
        load();
    }

    @Nonnull
    public abstract S getSubmitter(@Nonnull P player);

    @Nullable
    public S getSubmitter(@Nonnull UUID uuid) {
        return submitters.get(uuid);
    }

    @Nonnull
    public List<S> getSubmitters() {
        return new ArrayList<>(submitters.values());
    }

    protected abstract void load();

    public abstract void save();
}
