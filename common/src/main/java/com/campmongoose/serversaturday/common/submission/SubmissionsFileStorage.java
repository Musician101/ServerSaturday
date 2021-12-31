package com.campmongoose.serversaturday.common.submission;

import io.leangen.geantyref.TypeToken;
import io.musician101.musicianlibrary.java.configurate.ConfigurateLoader;
import io.musician101.musicianlibrary.java.storage.file.DataFileStorage;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

//TODO need to see why this is broken
//TODO builds overwriting each other for some fucking reason
//TODO rewards not being handed out properly
//TODO rewrite commands so it's all just in a single fucking command cuz fuck me
public final class SubmissionsFileStorage extends DataFileStorage<Submitter> {

    @Nonnull
    private final ConfigurateLoader loader;

    public SubmissionsFileStorage(@Nonnull File storageDir, @Nonnull ConfigurateLoader loader, @Nonnull String extension, @Nonnull TypeToken<Submitter> typeToken, @Nonnull TypeSerializerCollection typeSerializerCollection) {
        super(storageDir, loader, extension, typeToken, typeSerializerCollection);
        this.loader = loader;
    }

    @Nonnull
    @Override
    public Map<File, Exception> load() {
        Map<File, Exception> map = new HashMap<>();
        File[] files = storageDir.listFiles();
        if (files != null) {
            Arrays.stream(files).filter(file -> file.getName().endsWith(extension)).forEach(file -> {
                try {
                    ConfigurationNode node = loader.loader(file.toPath()).load();
                    data.add(new Submitter.Serializer().deserialize(Submitter.class, node));
                }
                catch (ConfigurateException e) {
                    map.put(file, e);
                }
            });
        }

        return map;
    }

    @Nonnull
    @Override
    public Map<File, Exception> save() {
        Map<File, Exception> map = new HashMap<>();
        data.forEach(submitter -> {
            File file = getPath(submitter).toFile();
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

                ConfigurationNode node = BasicConfigurationNode.root();
                new Submitter.Serializer().serialize(Submitter.class, submitter, node);
                loader.loader(getPath(submitter)).save(node);
            }
            catch (Exception e) {
                map.put(file, e);
            }
        });
        return map;
    }

    @Nonnull
    @Override
    protected Path getPath(Submitter submitter) {
        return new File(storageDir, submitter.getUUID() + extension).toPath();
    }
}
