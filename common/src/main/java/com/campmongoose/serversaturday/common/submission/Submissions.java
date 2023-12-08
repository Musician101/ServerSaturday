package com.campmongoose.serversaturday.common.submission;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Submissions {

    @NotNull
    protected final List<Submitter> submitters = new ArrayList<>();

    @NotNull
    public Submitter getSubmitter(@NotNull UUID uuid) {
        return getSubmitters().stream().filter(s -> uuid.equals(s.getUUID())).findFirst().orElseGet(() -> {
            Submitter submitter = new Submitter(uuid);
            submitters.add(submitter);
            return submitter;
        });
    }

    @NotNull
    public List<Submitter> getSubmitters() {
        return submitters;
    }

    protected void createDirectories(@NotNull Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
    }

    public abstract void load();

    public abstract void save();
}
