package com.campmongoose.serversaturday.paper.submission;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.SSLocation;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.campmongoose.serversaturday.paper.PaperServerSaturday.getPlugin;
import static com.google.common.base.Preconditions.checkNotNull;

public interface PaperSubmissionsUtil {

    @NotNull
    static Optional<Submitter> getSubmitter(@NotNull String name) {
        return getPlugin().getSubmissions().getSubmitters().stream().filter(s -> name.equalsIgnoreCase(getSubmitterName(s))).findFirst();
    }

    @NotNull
    static Build newBuild(@NotNull String name, @NotNull Location location) {
        return new Build(name, asSSLocation(location));
    }

    @NotNull
    static SSLocation asSSLocation(@NotNull Location location) {
        SSLocation ssLocation = new SSLocation(location.getWorld().getUID());
        ssLocation.setX(location.getX());
        ssLocation.setY(location.getY());
        ssLocation.setZ(location.getZ());
        ssLocation.setPitch(location.getPitch());
        ssLocation.setYaw(location.getYaw());
        return ssLocation;
    }

    @NotNull
    static Location asLocation(@NotNull SSLocation ssLocation) {
        return new Location(Bukkit.getWorld(ssLocation.getWorld()), ssLocation.getX(), ssLocation.getY(), ssLocation.getZ(), (float) ssLocation.getYaw(), (float) ssLocation.getPitch());
    }

    @NotNull
    static ConfigurationSection saveBuild(@NotNull Build build) {
        ConfigurationSection cs = new YamlConfiguration();
        cs.set(Reference.Config.NAME, build.getName());
        cs.set(Reference.Config.DESCRIPTION, build.getDescription());
        cs.set(Reference.Config.FEATURED, build.featured());
        cs.set(Reference.Config.LOCATION, asLocation(build.getLocation()));
        cs.set(Reference.Config.RESOURCE_PACK, build.getResourcePack());
        cs.set(Reference.Config.SUBMITTED, build.submitted());
        return cs;
    }

    @NotNull
    static SSLocation asSSLocation(@NotNull ConfigurationSection cs) {
        SSLocation location = new SSLocation(getUUID(cs));
        location.setX(cs.getDouble("x"));
        location.setY(cs.getDouble("y"));
        location.setZ(cs.getDouble("z"));
        location.setYaw(cs.getDouble("yaw"));
        location.setPitch(cs.getDouble("pitch"));
        return location;
    }

    @NotNull
    static Build loadBuild(@NotNull ConfigurationSection cs) {
        Build build = new Build(checkNotNull(cs.getString(Reference.Config.NAME)), asSSLocation(checkNotNull(cs.getConfigurationSection(Reference.Config.LOCATION))));
        build.setDescription(cs.getString(Reference.Config.DESCRIPTION, ""));
        build.setFeatured(cs.getBoolean(Reference.Config.FEATURED, false));
        build.setResourcePack(cs.getString(Reference.Config.RESOURCE_PACK, ""));
        build.setSubmitted(cs.getBoolean(Reference.Config.SUBMITTED, false));
        return build;
    }

    private static UUID getUUID(ConfigurationSection cs) {
        return UUID.fromString(checkNotNull(cs.getString(Reference.Config.UUID)));
    }

    @NotNull
    static String getSubmitterName(@NotNull Submitter submitter) {
        String name = Bukkit.getOfflinePlayer(submitter.getUUID()).getName();
        return name == null ? "null" : name;
    }

    @NotNull
    static Optional<Submitter> loadSubmitter(@NotNull Path path) {
        try {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(path.toFile());
            Submitter submitter = new Submitter(getUUID(yaml));
            yaml.getMapList(Reference.Config.BUILDS).stream().map(map -> {
                ConfigurationSection build = new YamlConfiguration();
                map.forEach((k, v) -> build.set(k.toString(), v));
                return build;
            }).map(PaperSubmissionsUtil::loadBuild).forEach(submitter::addBuild);
            return Optional.of(submitter);
        }
        catch (Exception e) {
            getPlugin().getSLF4JLogger().error(Reference.Messages.failedToReadFile(path), e);
            return Optional.empty();
        }
    }

    static void saveSubmitter(@NotNull Submitter submitter) {
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set(Reference.Config.UUID, submitter.getUUID().toString());
        yaml.set(Reference.Config.NAME, getSubmitterName(submitter));
        yaml.set(Reference.Config.BUILDS, submitter.getBuilds().stream().map(PaperSubmissionsUtil::saveBuild).collect(Collectors.toList()));
        Path storageDir = getPlugin().getDataFolder().toPath().resolve("submissions");
        Path path = storageDir.resolve(submitter.getUUID() + ".yml");
        try {
            if (Files.notExists(path)) {
                Files.createFile(path);
            }

            yaml.save(path.toFile());
        }
        catch (Exception e) {
            getPlugin().getSLF4JLogger().error(Reference.Messages.failedToWriteFile(path), e);
        }
    }
}
