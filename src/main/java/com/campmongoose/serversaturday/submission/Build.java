package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.submission.ConfigKey.NonRequiredKey;
import com.campmongoose.serversaturday.submission.ConfigKey.RequiredKey;
import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

@NullMarked
public final class Build {

    private final String id;
    private String description = "";
    private boolean featured = false;
    private Location location;
    private String name;
    private String resourcePack = "";
    private boolean submitted = false;

    Build(String id, String name, Location location) {
        this.name = name;
        this.location = location;
        this.id = id;
    }

    public boolean featured() {
        return featured;
    }

    public String description() {
        return description;
    }

    public void description(String description) {
        this.description = description;
    }

    public Location location() {
        return location;
    }

    public void location(Location location) {
        this.location = location;
    }

    public String name() {
        return name;
    }

    public void name(String name) {
        this.name = name;
    }

    public String resourcePack() {
        return resourcePack;
    }

    public void resourcePack(String resourcePack) {
        this.resourcePack = resourcePack;
    }

    public void featured(boolean featured) {
        this.featured = featured;
    }

    public void submitted(boolean submitted) {
        this.submitted = submitted;
    }

    public boolean submitted() {
        return submitted;
    }

    public String id() {
        return id;
    }

    public static class Serializer implements TypeSerializer<Build> {

        private static final NonRequiredKey<String> DESCRIPTION = ConfigKey.nonRequiredKey("description", String.class, "");
        private static final NonRequiredKey<Boolean> FEATURED = ConfigKey.nonRequiredKey("featured", Boolean.class, false);
        private static final RequiredKey<String> ID = ConfigKey.requiredKey("id", String.class);
        private static final RequiredKey<Location> LOCATION = ConfigKey.requiredKey("location", Location.class);
        private static final RequiredKey<String> NAME = ConfigKey.requiredKey("name", String.class);
        private static final NonRequiredKey<String> RESOURCE_PACK = ConfigKey.nonRequiredKey("resource-pack", String.class, "");
        private static final NonRequiredKey<Boolean> SUBMITTED = ConfigKey.nonRequiredKey("submitted", Boolean.class, false);

        @Override
        public Build deserialize(Type type, ConfigurationNode node) throws SerializationException {
            String id = ID.get(node);
            String name = NAME.get(node);
            Location location = LOCATION.get(node);
            Build build = new Build(id, name, location);
            build.description = DESCRIPTION.get(node);
            build.resourcePack = RESOURCE_PACK.get(node);
            build.featured = FEATURED.get(node);
            build.submitted = SUBMITTED.get(node);
            return build;
        }

        @Override
        public void serialize(Type type, @Nullable Build obj, ConfigurationNode node) throws SerializationException {
            if (obj == null) {
                return;
            }

            ID.set(node, obj.id());
            NAME.set(node, obj.name());
            LOCATION.set(node, obj.location());
            DESCRIPTION.set(node, obj.description());
            RESOURCE_PACK.set(node, obj.resourcePack());
            FEATURED.set(node, obj.featured());
            SUBMITTED.set(node, obj.submitted());
        }
    }
}
