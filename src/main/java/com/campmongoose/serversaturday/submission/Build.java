package com.campmongoose.serversaturday.submission;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Build {

    private static final String DESCRIPTION = "description";
    private static final String FEATURED = "featured";
    private static final String LOCATION = "location";
    private static final String NAME = "name";
    private static final String RESOURCE_PACK = "resource_pack";
    private static final String SUBMITTED = "submitted";

    @NotNull
    private String description = "";
    private boolean featured = false;
    @NotNull
    private Location location;
    @NotNull
    private String name;
    @NotNull
    private String resourcePack = "";
    private boolean submitted = false;

    public Build(@NotNull ConfigurationSection build) {
        this.description = build.getString(DESCRIPTION, "");
        this.featured = build.getBoolean(FEATURED, false);
        this.location = checkNotNull(build.getLocation(LOCATION));
        this.name = checkNotNull(build.getString(NAME));
        this.resourcePack = build.getString(RESOURCE_PACK, "");
        this.submitted = build.getBoolean(SUBMITTED, false);
    }

    public Build(@NotNull String name, @NotNull Location location) {
        this.name = name;
        this.location = location;
    }

    public boolean featured() {
        return featured;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NotNull String description) {
        this.description = description;
    }

    @NotNull
    public Location getLocation() {
        return location;
    }

    public void setLocation(@NotNull Location location) {
        this.location = location;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getResourcePack() {
        return resourcePack;
    }

    public void setResourcePack(@NotNull String resourcePack) {
        this.resourcePack = resourcePack;
    }

    @NotNull
    public ConfigurationSection save() {
        ConfigurationSection build = new YamlConfiguration();
        build.set(NAME, name);
        build.set(DESCRIPTION, description);
        build.set(FEATURED, featured);
        build.set(LOCATION, location);
        build.set(RESOURCE_PACK, resourcePack);
        build.set(SUBMITTED, submitted);
        return build;
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
}
