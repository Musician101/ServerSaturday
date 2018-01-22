package com.campmongoose.serversaturday.common.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;

//TODO allow resourcepack and description to be Text in Sponge
public abstract class Build<B extends Build<B, I, L, S>, I, L, S extends Submitter<B, I, L, S>> {

    @Nonnull
    protected List<String> description = Collections.singletonList("A Server Saturday Build");
    protected boolean featured = false;
    protected L location;
    @Nonnull
    protected String name;
    @Nonnull
    protected List<String> resourcePacks = Collections.singletonList(Config.VANILLA);
    protected boolean submitted = false;

    protected Build(@Nonnull String name, @Nonnull L location) {
        this.name = name;
        this.location = location;
    }

    public boolean featured() {
        return featured;
    }

    @Nonnull
    public List<String> getDescription() {
        return description;
    }

    public void setDescription(@Nonnull List<String> description) {
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
    public List<String> getResourcePacks() {
        return resourcePacks;
    }

    public void setResourcePacks(@Nonnull List<String> resourcePacks) {
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

    public interface Serializer<B extends Build<B, I, L, S>, I, L, S extends Submitter<B, I, L, S>> extends JsonDeserializer<B>, JsonSerializer<B> {

    }
}
