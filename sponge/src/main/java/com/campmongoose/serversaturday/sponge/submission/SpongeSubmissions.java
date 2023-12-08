package com.campmongoose.serversaturday.sponge.submission;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Submissions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.logging.log4j.Logger;

import static com.campmongoose.serversaturday.sponge.SpongeServerSaturday.getPlugin;

public class SpongeSubmissions extends Submissions {

    @Override
    public void load() {
        Path storageDir = getPlugin().getConfigDir().resolve("submissions");
        Logger logger = getPlugin().getLogger();
        try {
            createDirectories(storageDir);
        }
        catch (IOException e) {
            logger.error("Failed to read submitters directory!", e);
        }

        try(Stream<Path> stream = Files.list(storageDir)) {
            stream.map(SpongeSubmissionsUtil::loadSubmitter).filter(Optional::isPresent).map(Optional::get).forEach(submitters::add);
        }
        catch (IOException e) {
            logger.error("Failed to read submitters directory!", e);
        }
    }

    @Override
    public void save() {
        try {
            createDirectories(getPlugin().getConfigDir().resolve("submissions"));
        } catch (IOException e) {
            getPlugin().getLogger().error(Messages.FAILED_TO_READ_SUBMITTERS);
        }

        submitters.forEach(SpongeSubmissionsUtil::saveSubmitter);
    }
}
