package com.campmongoose.serversaturday.common.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import io.leangen.geantyref.TypeToken;
import io.musician101.musicianlibrary.java.minecraft.common.Location;
import io.musician101.musicianlibrary.java.storage.database.mongo.MongoSerializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bson.Document;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

public final class Submitter<T> {

    @Nonnull
    private final Map<String, Build<T>> builds = new HashMap<>();
    @Nonnull
    private final UUID uuid;
    @Nonnull
    private String name;

    public Submitter(@Nonnull String name, @Nonnull UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    @Nullable
    public Build<T> getBuild(@Nonnull String name) {
        return builds.get(name);
    }

    @Nonnull
    public List<Build<T>> getBuilds() {
        return new ArrayList<>(builds.values());
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    @Nonnull
    public UUID getUUID() {
        return uuid;
    }

    @Nonnull
    public Build<T> newBuild(@Nonnull String name, @Nonnull Location location) {
        builds.putIfAbsent(name, new Build<>(name, location));
        return builds.get(name);
    }

    public boolean removeBuild(@Nonnull String name) {
        return builds.remove(name) != null;
    }

    public void renameBuild(@Nonnull String newName, @Nonnull Build<T> build) {
        builds.remove(build.getName());
        build.setName(newName);
        builds.put(newName, build);
    }

    public static final class Serializer<T> implements MongoSerializable<Submitter<T>>, TypeSerializer<Submitter<T>> {

        private final Class<T> clazz;

        public Serializer(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Submitter<T> deserialize(Type type, ConfigurationNode node) throws SerializationException {
            String name = node.node(Config.NAME).getString();
            if (name == null) {
                throw new SerializationException("Submitter name cannot be null.");
            }

            UUID uuid = node.node(Config.UUID).get(UUID.class);
            if (uuid == null) {
                throw new SerializationException("Submitter UUID cannot be null.");
            }

            Submitter<T> submitter = new Submitter<>(name, uuid);
            node.node(Config.BUILDS).getList(new TypeToken<Build<T>>() {

            });
            return submitter;
        }

        @Override
        public Submitter<T> deserialize(@Nullable Document document) {
            if (document == null) {
                return null;
            }

            String name = document.getString(Config.NAME);
            if (name == null) {
                throw new IllegalStateException("Submitter name cannot be null.");
            }

            UUID uuid = document.get(Config.UUID, UUID.class);
            if (uuid == null) {
                throw new IllegalStateException("Submitter UUID cannot be null.");
            }

            Submitter<T> submitter = new Submitter<>(name, uuid);
            submitter.builds.putAll(document.getList(Config.BUILDS, Document.class, new ArrayList<>()).stream().map(d -> new Build.Serializer<>(clazz).deserialize(d)).collect(Collectors.toMap(Build::getName, v -> v)));
            return submitter;
        }

        @Override
        public Document serialize(@Nonnull Submitter<T> obj) {
            Document document = new Document();
            document.put(Config.NAME, obj.name);
            document.put(Config.UUID, obj.uuid);
            document.put(Config.BUILDS, obj.getBuilds().stream().map(build -> new Build.Serializer<>(clazz).serialize(build)).collect(Collectors.toList()));
            return document;
        }

        @Override
        public void serialize(Type type, @Nullable Submitter<T> obj, ConfigurationNode node) throws SerializationException {
            if (obj == null) {
                return;
            }

            node.node(Config.NAME).set(obj.name);
            node.node(Config.NAME).set(obj.uuid);
            node.node(Config.BUILDS).setList(new TypeToken<Build<T>>() {

            }, obj.getBuilds());
        }
    }
}
