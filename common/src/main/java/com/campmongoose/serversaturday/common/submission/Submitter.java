package com.campmongoose.serversaturday.common.submission;

import com.campmongoose.serversaturday.common.SSLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Submitter {

    @NotNull
    protected final List<Build> builds = new ArrayList<>();
    @NotNull
    private final UUID uuid;

    public Submitter(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    @NotNull
    public Optional<Build> getBuild(@NotNull String name) {
        return builds.stream().filter(s -> name.equalsIgnoreCase(s.getName())).findFirst();
    }

    @NotNull
    public List<Build> getBuilds() {
        return builds;
    }

    @NotNull
    public UUID getUUID() {
        return uuid;
    }

    public void newBuild(@NotNull String name, @NotNull SSLocation location) {
        addBuild(new Build(name, location));
    }

    public void addBuild(@NotNull Build build) {
        builds.add(build);
    }
}
