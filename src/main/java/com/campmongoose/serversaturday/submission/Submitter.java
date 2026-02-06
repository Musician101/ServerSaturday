package com.campmongoose.serversaturday.submission;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;
import org.spongepowered.configurate.util.Types;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NullMarked
public final class Submitter {

    private final List<Build> builds;
    private final UUID uuid;
    private String name;

    Submitter(Player player) {
        this(player.getUniqueId(), player.getName(), List.of());
    }

    private Submitter(UUID uuid, String name, List<Build> builds) {
        this.uuid = uuid;
        this.name = name;
        this.builds = builds;
    }

    public Optional<Build> getBuild(String name) {
        return builds.stream().filter(s -> name.equalsIgnoreCase(s.name())).findFirst();
    }

    public List<Build> builds() {
        return builds;
    }

    public String name() {
        String name = Bukkit.getOfflinePlayer(uuid).getName();
        if (name == null) {
            return this.name;
        }

        this.name = name;
        return name;
    }

    public UUID uniqueId() {
        return uuid;
    }

    public void newBuild(String id, String name, Location location) {
        builds.add(new Build(id, name, location));
    }

    public static class Serializer implements TypeSerializer<Submitter> {

        private static final ConfigKey<List<Build>> BUILDS = ConfigKey.nonRequiredKey("builds", Types.makeList(Build.class).getType(), List.of());
        private static final ConfigKey<String> NAME = ConfigKey.requiredKey("name", String.class);
        private static final ConfigKey<UUID> UUID = ConfigKey.requiredKey("uuid", UUID.class);

        @Override
        public Submitter deserialize(Type type, ConfigurationNode node) throws SerializationException {
            UUID uuid = UUID.get(node);
            String name = NAME.get(node);
            List<Build> builds = BUILDS.get(node);
            return new Submitter(uuid, name, builds);
        }

        @Override
        public void serialize(Type type, @Nullable Submitter obj, ConfigurationNode node) throws SerializationException {
            if (obj == null) {
                return;
            }

            UUID.set(node, obj.uniqueId());
            NAME.set(node, obj.name());
            BUILDS.set(node, obj.builds());
        }
    }
}
