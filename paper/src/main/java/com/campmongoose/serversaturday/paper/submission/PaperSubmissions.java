package com.campmongoose.serversaturday.paper.submission;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Submissions;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import static com.campmongoose.serversaturday.paper.PaperServerSaturday.getPlugin;

public class PaperSubmissions extends Submissions {

    @Override
    public void load() {
        Path storageDir = getPlugin().getDataFolder().toPath().resolve("submissions");
        Logger logger = getPlugin().getSLF4JLogger();
        try {
            createDirectories(storageDir);
        }
        catch (IOException e) {
            logger.error("Failed to read submitters directory!", e);
        }

        try(Stream<Path> stream = Files.list(storageDir)) {
            stream.map(PaperSubmissionsUtil::loadSubmitter).filter(Optional::isPresent).map(Optional::get).forEach(submitters::add);
        }
        catch (IOException e) {
            logger.error("Failed to read submitters directory!", e);
        }
    }

    @Override
    public void save() {
        try {
            createDirectories(getPlugin().getDataFolder().toPath().resolve("submissions"));
        } catch (IOException e) {
            getPlugin().getSLF4JLogger().error(Messages.FAILED_TO_READ_SUBMITTERS);
        }

        submitters.forEach(PaperSubmissionsUtil::saveSubmitter);
    }
}
