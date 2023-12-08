package com.campmongoose.serversaturday.sponge.submission;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.SSLocation;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.WorldManager;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import org.spongepowered.math.vector.Vector3d;

import static com.campmongoose.serversaturday.sponge.SpongeServerSaturday.getPlugin;
import static com.google.common.base.Preconditions.checkNotNull;

public interface SpongeSubmissionsUtil {

    static SSLocation asSSLocation(ServerPlayer player) {
        return new SpongeLocation(player.serverLocation(), player.rotation());
    }

    class SpongeLocation extends SSLocation {

        public double getRoll() {
            return roll;
        }

        public void setRoll(double roll) {
            this.roll = roll;
        }

        private double roll;

        public SpongeLocation(@NotNull UUID world) {
            super(world);
        }

        public SpongeLocation(@NotNull ServerLocation location, @NotNull Vector3d rotation) {
            this(location.world().uniqueId(), location.x(), location.y(), location.z(), rotation.x(), rotation.y(), rotation.z());
        }

        public SpongeLocation(@NotNull UUID world, double x, double y, double z, double pitch, double yaw, double roll) {
            super(world, x, y, z, pitch, yaw);
            this.roll = roll;
        }
    }

    @NotNull
    static Optional<Submitter> getSubmitter(@NotNull String name) {
        return getPlugin().getSubmissions().getSubmitters().stream().filter(s -> name.equalsIgnoreCase(getSubmitterName(s))).findFirst();
    }

    @NotNull
    static Build newBuild(@NotNull String name, @NotNull ServerLocation location, @NotNull Vector3d rotation) {
        return new Build(name, asSSLocation(location, rotation));
    }

    @NotNull
    static SpongeLocation asSSLocation(@NotNull ServerLocation location, @NotNull Vector3d rotation) {
        SpongeLocation ssLocation = new SpongeLocation(location.world().uniqueId());
        ssLocation.setX(location.x());
        ssLocation.setY(location.y());
        ssLocation.setZ(location.z());
        ssLocation.setPitch(rotation.x());
        ssLocation.setYaw(rotation.y());
        ssLocation.setRoll(rotation.z());
        return ssLocation;
    }

    @NotNull
    static ServerLocation asLocation(@NotNull SSLocation ssLocation) {
        WorldManager worldManager = Sponge.server().worldManager();
        return ServerLocation.of(worldManager.worldKey(ssLocation.getWorld()).orElse(worldManager.worldKeys().get(0)), ssLocation.getX(), ssLocation.getY(), ssLocation.getZ());
    }

    @NotNull
    static Vector3d asRotation(@NotNull SSLocation ssLocation) {
        return new Vector3d(ssLocation.getPitch(), ssLocation.getYaw(), ((SpongeLocation) ssLocation).roll);
    }

    private static <V> void setValue(ConfigurationNode node, String key, V value) {
        try {
            node.node(key).set(value);
        }
        catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    static ConfigurationNode saveBuild(@NotNull Build build) {
        ConfigurationNode node = BasicConfigurationNode.root();
        try {
            setValue(node, Reference.Config.NAME, build.getName());
            setValue(node, Reference.Config.DESCRIPTION, build.getDescription());
            setValue(node, Reference.Config.FEATURED, build.featured());
            ConfigurationNode locationNode = BasicConfigurationNode.root();
            SpongeLocation location = (SpongeLocation) build.getLocation();
            setValue(locationNode, "x", location.getX());
            setValue(locationNode, "y", location.getY());
            setValue(locationNode, "z", location.getZ());
            setValue(locationNode, "pitch", location.getPitch());
            setValue(locationNode, "yaw", location.getYaw());
            setValue(locationNode, "roll", location.getRoll());
            setValue(node, Reference.Config.LOCATION, build.getLocation());
            node.node(Reference.Config.RESOURCE_PACK).set(build.getResourcePack());
            setValue(node, Reference.Config.SUBMITTED, build.submitted());
        }
        catch (SerializationException e) {
            throw new RuntimeException(e);
        }

        return node;
    }

    private static <V> V getValue(ConfigurationNode node, String key, Class<V> clazz) {
        try {
            return node.node(key).get(clazz);
        }
        catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    private static <V> V getValue(ConfigurationNode node, String key, V defaultValue, Class<V> clazz) {
        if (node.node(key).empty()) {
            return defaultValue;
        }

        try {
            return node.node(key).get(clazz);
        }
        catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    static SpongeLocation asSSLocation(@NotNull ConfigurationNode node) {
        SpongeLocation location = new SpongeLocation(getUUID(node));
        location.setX(getValue(node, "x", Double.class));
        location.setY(getValue(node, "y", Double.class));
        location.setZ(getValue(node, "z", Double.class));
        location.setYaw(getValue(node, "yaw", Double.class));
        location.setPitch(getValue(node, "pitch", Double.class));
        location.setRoll(getValue(node, "roll", Double.class));
        return location;
    }

    @NotNull
    static Build loadBuild(@NotNull ConfigurationNode node) {
        Build build = new Build(checkNotNull(node.getString(Reference.Config.NAME)), asSSLocation(checkNotNull(node.node(Reference.Config.LOCATION))));
        build.setDescription(getValue(node, Reference.Config.DESCRIPTION, "", String.class));
        build.setFeatured(getValue(node, Reference.Config.FEATURED, false, Boolean.class));
        build.setResourcePack(getValue(node, Reference.Config.RESOURCE_PACK, "", String.class));
        build.setSubmitted(getValue(node, Reference.Config.SUBMITTED, false, Boolean.class));
        return build;
    }

    private static UUID getUUID(ConfigurationNode cs) {
        return UUID.fromString(checkNotNull(cs.getString(Reference.Config.UUID)));
    }

    @NotNull
    static String getSubmitterName(@NotNull Submitter submitter) {
        try {
            return Sponge.server().gameProfileManager().basicProfile(submitter.getUUID()).get().name().orElse("null");
        }
        catch (InterruptedException | ExecutionException e) {
            return "null";
        }
    }

    @NotNull
    static Optional<Submitter> loadSubmitter(@NotNull Path path) {
        try {
            ConfigurationNode node = YamlConfigurationLoader.builder().file(path.toFile()).build().load();
            Submitter submitter = new Submitter(getUUID(node));
            node.node(Reference.Config.BUILDS).childrenList().stream().map(SpongeSubmissionsUtil::loadBuild).forEach(submitter::addBuild);
            return Optional.of(submitter);
        }
        catch (Exception e) {
            getPlugin().getLogger().error(Reference.Messages.failedToReadFile(path), e);
            return Optional.empty();
        }
    }

    static void saveSubmitter(@NotNull Submitter submitter) {
        ConfigurationNode node = BasicConfigurationNode.root();
        setValue(node, Reference.Config.UUID, submitter.getUUID());
        setValue(node, Reference.Config.NAME, getSubmitterName(submitter));
        setValue(node, Reference.Config.BUILDS, submitter.getBuilds().stream().map(SpongeSubmissionsUtil::saveBuild).toList());
        Path storageDir = getPlugin().getConfigDir().resolve("submissions");
        Path path = storageDir.resolve(submitter.getUUID() + ".yml");
        try {
            if (Files.notExists(storageDir)) {
                Files.createDirectories(storageDir);
            }

            if (Files.notExists(path)) {
                Files.createFile(path);
            }

            YamlConfigurationLoader.builder().file(path.toFile()).build().save(node);
        }
        catch (Exception e) {
            getPlugin().getLogger().error(Reference.Messages.failedToWriteFile(path), e);
        }
    }
}
