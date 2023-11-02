package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.Reference.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Submitter {

    @NotNull
    private final List<Build> builds = new ArrayList<>();
    @NotNull
    private final UUID uuid;

    public Submitter(@NotNull ConfigurationSection submitter) {
        this.uuid = UUID.fromString(checkNotNull(submitter.getString(Config.UUID)));
        submitter.getMapList(Config.BUILDS).stream().map(map -> {
            ConfigurationSection build = new YamlConfiguration();
            map.forEach((k, v) -> build.set(k.toString(), v));
            return build;
        }).map(Build::new).forEach(builds::add);
    }

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
    public String getName() {
        String name = Bukkit.getOfflinePlayer(uuid).getName();
        return name == null ? "null" : name;
    }

    @NotNull
    public UUID getUUID() {
        return uuid;
    }

    public void newBuild(@NotNull String name, @NotNull Location location) {
        builds.add(new Build(name, location));
    }

    @NotNull
    public YamlConfiguration save() {
        YamlConfiguration submitter = new YamlConfiguration();
        submitter.set(Config.UUID, uuid.toString());
        submitter.set(Config.NAME, getName());
        submitter.set(Config.BUILDS, builds.stream().map(Build::save).collect(Collectors.toList()));
        return submitter;
    }
}
