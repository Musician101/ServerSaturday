package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.ExceptionUtils;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

@NullMarked
public final class Submissions {

    private final List<Submitter> submitters = new ArrayList<>();

    public Optional<Submitter> getSubmitter(String name) {
        return submitters.stream().filter(s -> name.equalsIgnoreCase(s.name())).findFirst();
    }

    public Submitter getSubmitter(Player player) {
        UUID uuid = player.getUniqueId();
        return submitters.stream().filter(s -> uuid.equals(s.uniqueId())).findFirst().orElseGet(() -> {
            Submitter submitter = new Submitter(player);
            submitters.add(submitter);
            return submitter;
        });
    }

    public Optional<Build> build(NamespacedKey key) {
        String submitterName = key.namespace();
        String buildName = key.getKey();
        return getSubmitter(submitterName).flatMap(submitter -> submitter.getBuild(buildName));
    }

    public List<Submitter> getSubmitters() {
        return submitters;
    }

    public void load() throws IOException {
        Path storageDir = getPlugin().getDataPath().resolve("submissions");
        if (Files.notExists(storageDir)) {
            Files.createDirectories(storageDir);
        }

        try (Stream<Path> stream = Files.list(storageDir)) {
            submitters.clear();
            ExceptionUtils.throwIOException("One or more errors occurred while loading submitters.", stream.map(this::loader).map(loader -> {
                try {
                    submitters.add(loader.load().require(Submitter.class));
                    return null;
                }
                catch (IOException e) {
                    return e;
                }
            }).filter(Objects::nonNull));
        }
    }

    private YamlConfigurationLoader loader(Path path) {
        TypeSerializerCollection tsc = TypeSerializerCollection.defaults().childBuilder()
                .register(Submitter.class, new Submitter.Serializer())
                .register(Build.class, new Build.Serializer())
                .register(Location.class, new LocationSerializer()).build();
        return YamlConfigurationLoader.builder().nodeStyle(NodeStyle.BLOCK).path(path).defaultOptions(options -> options.serializers(tsc)).build();
    }

    public void save() throws IOException {
        Path storageDir = getPlugin().getDataFolder().toPath().resolve("submissions");
        if (Files.notExists(storageDir)) {
            Files.createDirectories(storageDir);
        }

        ExceptionUtils.throwIOException("One or more errors occurred while saving submitters.", submitters.stream().map(submitter -> {
            try {
                Path path = storageDir.resolve(submitter.uniqueId() + ".yml");
                YamlConfigurationLoader loader = loader(path);
                ConfigurationNode node = loader.createNode();
                node.set(submitter);
                loader.save(node);
                return null;
            }
            catch (IOException e) {
                return e;
            }
        }).filter(Objects::nonNull));
    }
}
