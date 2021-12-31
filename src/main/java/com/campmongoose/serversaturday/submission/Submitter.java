package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.Reference.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Submitter {

    @Nonnull
    private final List<Build> builds = new ArrayList<>();
    @Nonnull
    private final UUID uuid;

    public Submitter(@Nonnull ConfigurationSection submitter) {
        this.uuid = UUID.fromString(checkNotNull(submitter.getString(Config.UUID)));
        submitter.getMapList(Config.BUILDS).stream().map(map -> {
            ConfigurationSection build = new YamlConfiguration();
            map.forEach((k, v) -> build.set(k.toString(), v));
            return build;
        }).map(Build::new).forEach(builds::add);
    }

    public Submitter(@Nonnull UUID uuid) {
        this.uuid = uuid;
    }

    @Nonnull
    public Optional<Build> getBuild(@Nonnull String name) {
        return builds.stream().filter(s -> name.equalsIgnoreCase(s.getName())).findFirst();
    }

    @Nonnull
    public List<Build> getBuilds() {
        return builds;
    }

    @Nonnull
    public String getName() {
        String name = Bukkit.getOfflinePlayer(uuid).getName();
        return name == null ? "null" : name;
    }

    @Nonnull
    public UUID getUUID() {
        return uuid;
    }

    @Nonnull
    public Build newBuild(@Nonnull String name, @Nonnull Location location) {
        Build build = new Build(name, location);
        builds.add(build);
        return build;
    }

    @Nonnull
    public YamlConfiguration save() {
        YamlConfiguration submitter = new YamlConfiguration();
        submitter.set(Config.UUID, uuid.toString());
        submitter.set(Config.NAME, getName());
        submitter.set(Config.BUILDS, builds.stream().map(Build::save).collect(Collectors.toList()));
        return submitter;
    }
}
