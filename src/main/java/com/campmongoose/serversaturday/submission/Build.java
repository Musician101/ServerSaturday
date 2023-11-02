package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.Reference.Config;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Build {

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
        this.description = build.getString(Config.DESCRIPTION, "");
        this.featured = build.getBoolean(Config.FEATURED, false);
        this.location = checkNotNull(build.getLocation(Config.LOCATION));
        this.name = checkNotNull(build.getString(Config.NAME));
        this.resourcePack = build.getString(Config.RESOURCE_PACK, "");
        this.submitted = build.getBoolean(Config.SUBMITTED, false);
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
        build.set(Config.NAME, name);
        build.set(Config.DESCRIPTION, description);
        build.set(Config.FEATURED, featured);
        build.set(Config.LOCATION, location);
        build.set(Config.RESOURCE_PACK, resourcePack);
        build.set(Config.SUBMITTED, submitted);
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
