package com.campmongoose.serversaturday.common.submission;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public abstract class Build<ItemStack, Location, S extends Submitter<?, ?, ?>, Description> {

    @Nonnull
    protected List<Description> description = new ArrayList<>();
    protected boolean featured = false;
    @Nonnull
    protected Location location;
    @Nonnull
    protected String name;
    @Nonnull
    protected List<Description> resourcePacks = new ArrayList<>();
    protected boolean submitted = false;

    protected Build(@Nonnull String name, @Nonnull Location location) {
        this.name = name;
        this.location = location;
    }

    public boolean featured() {
        return featured;
    }

    @Nonnull
    public List<Description> getDescription() {
        return description;
    }

    public void setDescription(@Nonnull List<Description> description) {
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
    public abstract ItemStack getMenuRepresentation(@Nonnull S submitter);

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    @Nonnull
    public List<Description> getResourcePacks() {
        return resourcePacks;
    }

    public void setResourcePacks(@Nonnull List<Description> resourcePacks) {
        this.resourcePacks = resourcePacks;
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
