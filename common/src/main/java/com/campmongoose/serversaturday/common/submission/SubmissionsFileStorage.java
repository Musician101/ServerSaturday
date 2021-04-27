package com.campmongoose.serversaturday.common.submission;

import io.leangen.geantyref.TypeToken;
import io.musician101.musicianlibrary.java.configurate.ConfigurateLoader;
import io.musician101.musicianlibrary.java.storage.file.DataFileStorage;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

public final class SubmissionsFileStorage<T> extends DataFileStorage<Submitter<T>> {

    @Nonnull
    protected final File dir = null;
    //@Nonnull
    //protected final Gson gson;
    @Nonnull
    protected final Map<UUID, Submitter<T>> submitters = new HashMap<>();

    public SubmissionsFileStorage(@Nonnull File storageDir, @Nonnull ConfigurateLoader loader, @Nonnull String extension, @Nonnull TypeToken<Submitter<T>> typeToken, @Nullable TypeSerializerCollection typeSerializerCollection) {
        super(storageDir, loader, extension, typeToken, typeSerializerCollection);
    }

    @Nonnull
    @Override
    protected Path getPath(Submitter<T> submitter) {
        return new File(storageDir, submitter.getUUID() + extension).toPath();
    }

    /*public Submissions(@Nonnull File configDir, @Nonnull Gson gson) {
        this.dir = new File(configDir, "submitters");
        this.gson = gson;
        load();
    }*/

    @Nonnull
    public Submitter<T> getSubmitter(@Nonnull String name, @Nonnull UUID uuid) {
        submitters.putIfAbsent(uuid, new Submitter<>(name, uuid));
        return submitters.get(uuid);
    }

    @Nullable
    public Submitter<T> getSubmitter(@Nonnull UUID uuid) {
        return submitters.get(uuid);
    }

    @Nonnull
    public List<Submitter<T>> getSubmitters() {
        return new ArrayList<>(submitters.values());
    }
}
