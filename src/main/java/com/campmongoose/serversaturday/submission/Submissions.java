package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.ServerSaturday;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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
        ServerSaturday plugin = ServerSaturday.getInstance();
        File storageDir = new File(plugin.getDataFolder(), "submissions");
        storageDir.mkdirs();
        File[] files = storageDir.listFiles();
        if (files != null) {
            Arrays.stream(files).forEach(file -> {
                try {
                    submitters.add(new Submitter(YamlConfiguration.loadConfiguration(file)));
                }
                catch (Exception e) {
                    plugin.getSLF4JLogger().error(Messages.failedToReadFile(file), e);
                }
            });
        }
    }

    public void save() {
        ServerSaturday plugin = ServerSaturday.getInstance();
        File storageDir = new File(plugin.getDataFolder(), "submissions");
        storageDir.mkdirs();
        submitters.forEach(submitter -> {
            File file = new File(storageDir, submitter.getUUID() + ".yml");
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

                submitter.save().save(file);
            }
            catch (Exception e) {
                plugin.getSLF4JLogger().error(Messages.failedToWriteFile(file), e);
            }
        });
    }
}
