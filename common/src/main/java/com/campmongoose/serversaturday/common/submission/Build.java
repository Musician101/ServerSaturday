package com.campmongoose.serversaturday.common.submission;

import com.campmongoose.serversaturday.common.SSLocation;
import org.jetbrains.annotations.NotNull;

public class Build {

    @NotNull
    private String description = "";
    private boolean featured = false;
    @NotNull
    private SSLocation location;
    @NotNull
    private String name;
    @NotNull
    private String resourcePack = "";
    private boolean submitted = false;

    public Build(@NotNull String name, @NotNull SSLocation location) {
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
    public SSLocation getLocation() {
        return location;
    }

    public void setLocation(@NotNull SSLocation location) {
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
