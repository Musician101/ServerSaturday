package com.campmongoose.serversaturday.common.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

public abstract class AbstractBuild<I, L, S extends AbstractSubmitter> {

    @Nonnull
    protected List<String> description = Collections.singletonList("A Server Saturday Build");
    protected boolean featured = false;
    protected L location;
    @Nonnull
    protected String name;
    @Nonnull
    protected List<String> resourcePack = Collections.singletonList(Config.VANILLA);
    protected boolean submitted = false;

    public AbstractBuild(@Nonnull String name) {
        this.name = name;
    }

    public AbstractBuild(@Nonnull String name, @Nonnull L location) {
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
    public List<String> getResourcePack() {
        return resourcePack;
    }

    public void setResourcePack(@Nonnull List<String> resourcePack) {
        this.resourcePack = resourcePack;
    }

    @Nonnull
    public abstract Map<String, Object> serialize();

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
