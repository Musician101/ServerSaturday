package com.campmongoose.serversaturday.submission;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

public final class Submissions {

    @NotNull
    private final List<Submitter> submitters = new ArrayList<>();

    @NotNull
    public Optional<Submitter> getSubmitter(@NotNull String name) {
        return submitters.stream().filter(s -> name.equalsIgnoreCase(s.getName())).findFirst();
    }

    @NotNull
    public Submitter getSubmitter(Player player) {
        UUID uuid = player.getUniqueId();
        return submitters.stream().filter(s -> uuid.equals(s.getUUID())).findFirst().orElseGet(() -> {
            Submitter submitter = new Submitter(uuid);
            submitters.add(submitter);
            return submitter;
        });
    }

    @NotNull
    public List<Submitter> getSubmitters() {
        return submitters;
    }

    public void load() {
        Path storageDir = getPlugin().getDataFolder().toPath().resolve("submissions");
        if (Files.notExists(storageDir)) {
            try {
                Files.createDirectories(storageDir);
            }
            catch (IOException e) {
                getPlugin().getSLF4JLogger().error("Failed to read " + storageDir.getFileName(), e);
                return;
            }
        }

        try (Stream<Path> stream = Files.list(storageDir)) {
            submitters.clear();
            stream.forEach(path -> {
                try {
                    submitters.add(new Submitter(YamlConfiguration.loadConfiguration(path.toFile())));
                }
                catch (Exception e) {
                    getPlugin().getSLF4JLogger().error("Failed to read " + path.getFileName(), e);
                }
            });
        }
        catch (IOException e) {
            getPlugin().getSLF4JLogger().error("Failed to read submitters directory!", e);
        }
    }

    public void save() {
        Path storageDir = getPlugin().getDataFolder().toPath().resolve("submissions");
        if (Files.notExists(storageDir)) {
            try {
                Files.createDirectories(storageDir);
            }
            catch (IOException e) {
                getPlugin().getSLF4JLogger().error("Failed to create submissions folder.");
                return;
            }
        }

        submitters.forEach(submitter -> {
            Path path = storageDir.resolve(submitter.getUUID() + ".yml");
            try {
                if (Files.notExists(path)) {
                    Files.createFile(path);
                }

                submitter.save().save(path.toFile());
            }
            catch (Exception e) {
                getPlugin().getSLF4JLogger().error("Failed to write " + path.getFileName(), e);
            }
        });
    }
}
