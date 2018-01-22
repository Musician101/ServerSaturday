package com.campmongoose.serversaturday.common.submission;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public abstract class Build<B extends Build<B, I, L, S, T>, I, L, S extends Submitter<B, I, L, S, T>, T> {

    @Nonnull
    protected List<T> description = new ArrayList<>();
    protected boolean featured = false;
    @Nonnull
    protected L location;
    @Nonnull
    protected String name;
    @Nonnull
    protected List<T> resourcePacks = new ArrayList<>();
    protected boolean submitted = false;

    protected Build(@Nonnull String name, @Nonnull L location) {
        this.name = name;
        this.location = location;
    }

    public boolean featured() {
        return featured;
    }

    @Nonnull
    public List<T> getDescription() {
        return description;
    }

    public void setDescription(@Nonnull List<T> description) {
        this.description = description;
    }

    @Nonnull
    public L getLocation() {
        return location;
    }

    public void setLocation(@Nonnull L location) {
        this.location = location;
    }

    @Nonnull
    public abstract I getMenuRepresentation(@Nonnull S submitter);

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    @Nonnull
    public List<T> getResourcePacks() {
        return resourcePacks;
    }

    public void setResourcePacks(@Nonnull List<T> resourcePacks) {
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

    public interface Serializer<B extends Build<B, I, L, S, T>, I, L, S extends Submitter<B, I, L, S, T>, T> extends JsonDeserializer<B>, JsonSerializer<B> {

    }
}
