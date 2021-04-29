package com.campmongoose.serversaturday.common.submission;

import io.leangen.geantyref.TypeToken;
import io.musician101.musicianlibrary.java.configurate.ConfigurateLoader;
import io.musician101.musicianlibrary.java.storage.file.DataFileStorage;
import java.io.File;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

public final class SubmissionsFileStorage<T> extends DataFileStorage<Submitter<T>> {

    public SubmissionsFileStorage(@Nonnull File storageDir, @Nonnull ConfigurateLoader loader, @Nonnull String extension, @Nonnull TypeToken<Submitter<T>> typeToken, @Nullable TypeSerializerCollection typeSerializerCollection) {
        super(storageDir, loader, extension, typeToken, typeSerializerCollection);
    }

    @Nonnull
    @Override
    protected Path getPath(Submitter<T> submitter) {
        return new File(storageDir, submitter.getUUID() + extension).toPath();
    }
}
