package com.campmongoose.serversaturday.common.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import io.leangen.geantyref.TypeToken;
import io.musician101.musicianlibrary.java.minecraft.common.Location;
import io.musician101.musicianlibrary.java.storage.database.mongo.MongoSerializable;
import java.lang.reflect.Type;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bson.Document;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

public final class Build {

    @Nonnull
    private String description = "";
    private boolean featured = false;
    @Nonnull
    private Location location;
    @Nonnull
    private String name;
    @Nonnull
    private String resourcePack = "";
    private boolean submitted = false;

    public Build(@Nonnull String name, @Nonnull Location location) {
        this.name = name;
        this.location = location;
    }

    public boolean featured() {
        return featured;
    }

    @Nonnull
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nonnull String description) {
        this.description = description;
    }

    @Nonnull
    public Location getLocation() {
        return location;
    }

    public void setLocation(@Nonnull Location location) {
        this.location = location;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    @Nonnull
    public String getResourcePack() {
        return resourcePack;
    }

    public void setResourcePack(@Nonnull String resourcePack) {
        this.resourcePack = resourcePack;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public boolean submitted() {
        return submitted;
    }

    public record Serializer() implements MongoSerializable<Build>, TypeSerializer<Build> {

        @Override
        public Build deserialize(Type type, ConfigurationNode node) throws SerializationException {
            if (!type.equals(new TypeToken<Submitter>() {

            }.getType())) {
                return null;
            }

            String name = node.node(Config.NAME).getString();
            if (name == null) {
                throw new SerializationException("Build name can not be null.");
            }

            Location location = node.node(Config.LOCATION).get(Location.class);
            if (location == null) {
                throw new SerializationException("Location can not be null.");
            }

            Build build = new Build(name, location);
            build.featured = node.node(Config.FEATURED).getBoolean();
            build.submitted = node.node(Config.SUBMITTED).getBoolean();
            build.resourcePack = node.node(Config.RESOURCE_PACK).getString("");
            build.description = node.node(Config.DESCRIPTION).getString("");
            return build;
        }

        @Override
        public Build deserialize(@Nullable Document document) {
            if (document == null) {
                return null;
            }

            String name = document.getString(Config.NAME);
            if (name == null) {
                throw new IllegalArgumentException("Build name can not be null.");
            }

            Document locationDocument = document.get(Config.LOCATION, Document.class);
            if (locationDocument == null) {
                throw new IllegalArgumentException("Location can not be null.");
            }

            Location location = new Location.Serializer().deserialize(locationDocument);
            Build build = new Build(name, location);
            build.featured = document.getBoolean(Config.FEATURED);
            build.submitted = document.getBoolean(Config.SUBMITTED);
            build.resourcePack = document.getString(Config.RESOURCE_PACK);
            build.description = document.getString(Config.DESCRIPTION);
            return build;
        }

        @Override
        public Document serialize(@Nonnull Build obj) {
            Document document = new Document();
            document.put(Config.NAME, obj.name);
            document.put(Config.LOCATION, new Location.Serializer().serialize(obj.location));
            document.put(Config.FEATURED, obj.featured);
            document.put(Config.SUBMITTED, obj.submitted);
            document.put(Config.DESCRIPTION, obj.description);
            document.put(Config.RESOURCE_PACK, obj.resourcePack);
            return document;
        }

        @Override
        public void serialize(Type type, @Nullable Build obj, ConfigurationNode node) throws SerializationException {
            if (obj == null || !type.equals(new TypeToken<Submitter>() {

            }.getType())) {
                return;
            }

            node.node(Config.NAME).set(obj.name);
            node.node(Config.LOCATION).set(Location.class, obj.location);
            node.node(Config.FEATURED).set(obj.featured);
            node.node(Config.SUBMITTED).set(obj.submitted);
            node.node(Config.DESCRIPTION).set(obj.description);
            node.node(Config.RESOURCE_PACK).set(obj.resourcePack);
        }
    }
}
