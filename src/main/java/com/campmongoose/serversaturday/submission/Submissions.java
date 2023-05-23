package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.Reference.Messages;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

@SuppressWarnings("BlockingMethodInNonBlockingContext")
public final class Submissions {

    @Nonnull
    private final List<Submitter> submitters = new ArrayList<>();

    @Nonnull
    public Optional<Submitter> getSubmitter(@Nonnull String name) {
        return submitters.stream().filter(s -> name.equalsIgnoreCase(s.getName())).findFirst();
    }

    @Nonnull
    public Submitter getSubmitter(Player player) {
        UUID uuid = player.getUniqueId();
        return submitters.stream().filter(s -> uuid.equals(s.getUUID())).findFirst().orElseGet(() -> {
            Submitter submitter = new Submitter(uuid);
            submitters.add(submitter);
            return submitter;
        });
    }

    @Nonnull
    public List<Submitter> getSubmitters() {
        return submitters;
    }

    public void load() {
        Path storageDir = getPlugin().getDataFolder().toPath().resolve("submissions");
        try(Stream<Path> stream = Files.list(storageDir)) {
            stream.map(Path::toFile).forEach(f -> {
                try {
                    submitters.add(new Submitter(YamlConfiguration.loadConfiguration(f)));
                }
                catch (Exception e) {
                    getPlugin().getSLF4JLogger().error(Messages.failedToReadFile(f), e);
                }
            });
        }
        catch (IOException e) {
            getPlugin().getSLF4JLogger().error("Failed to read submitters directory!", e);
        }
    }

    public void save() {
        Path storageDir = getPlugin().getDataFolder().toPath().resolve("submissions");
        submitters.forEach(submitter -> {
            Path path = storageDir.resolve(submitter.getUUID() + ".yml");
            try {
                Files.createFile(path);
                submitter.save().save(path.toFile());
            }
            catch (Exception e) {
                getPlugin().getSLF4JLogger().error(Messages.failedToWriteFile(path), e);
            }
        });
    }
}
