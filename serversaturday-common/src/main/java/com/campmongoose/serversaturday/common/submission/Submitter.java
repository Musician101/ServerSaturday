package com.campmongoose.serversaturday.common.submission;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Submitter<B extends Build<B, I, L, S>, I, L, S extends Submitter<B, I, L, S>> {

    protected final Map<String, B> builds = new HashMap<>();
    @Nonnull
    protected final UUID uuid;
    @Nonnull
    protected String name;

    protected Submitter(@Nonnull String name, @Nonnull UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    @Nullable
    public B getBuild(@Nonnull String name) {
        return builds.get(name);
    }

    @Nonnull
    public List<B> getBuilds() {
        return new ArrayList<>(builds.values());
    }

    @Nonnull
    public abstract I getMenuRepresentation();

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    @Nonnull
    public UUID getUUID() {
        return uuid;
    }

    @Nonnull
    public abstract B newBuild(@Nonnull String name, @Nonnull L location);

    public boolean removeBuild(@Nonnull String name) {
        return builds.remove(name) != null;
    }

    public void renameBuild(String newName, B build) {
        builds.remove(build.getName());
        build.setName(newName);
        builds.put(newName, build);
    }

    public interface Serializer<B extends Build<B, I, L, S>, I, L, S extends Submitter<B, I, L, S>> extends JsonDeserializer<S>, JsonSerializer<S> {

    }
}
