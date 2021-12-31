package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.Reference.Config;
import javax.annotation.Nonnull;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import static com.google.common.base.Preconditions.checkNotNull;

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

    public Build(@Nonnull ConfigurationSection build) {
        this.description = build.getString(Config.DESCRIPTION, "");
        this.featured = build.getBoolean(Config.FEATURED, false);
        this.location = checkNotNull(build.getLocation(Config.LOCATION));
        this.name = checkNotNull(build.getString(Config.NAME));
        this.resourcePack = build.getString(Config.RESOURCE_PACK, "");
        this.submitted = build.getBoolean(Config.SUBMITTED, false);
    }

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

    @Nonnull
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
