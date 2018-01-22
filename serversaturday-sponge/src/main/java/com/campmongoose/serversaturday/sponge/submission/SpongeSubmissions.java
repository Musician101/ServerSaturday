package com.campmongoose.serversaturday.sponge.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.common.submission.Submissions;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.entity.living.player.Player;

public class SpongeSubmissions extends Submissions<Player, SpongeSubmitter> {

    public SpongeSubmissions(@Nonnull File storageDir) {
        super(storageDir);
    }

    @Nonnull
    @Override
    public SpongeSubmitter getSubmitter(@Nonnull Player player) {
        submitters.putIfAbsent(player.getUniqueId(), new SpongeSubmitter(player));
        return submitters.get(player.getUniqueId());
    }

    @Override
    public void load() {
        SpongeServerSaturday.instance().map(ServerSaturday::getLogger).ifPresent(logger -> {
            if (dir.mkdirs()) {
                logger.info(Messages.newFile(dir));
            }

            File[] files = dir.listFiles();
            if (files != null) {
                Stream.of(files).filter(file -> file.getName().endsWith(Config.HOCON_EXT))
                        .forEach(file -> {
                            try {
                                ConfigurationNode cn = HoconConfigurationLoader.builder().setFile(file).build().load();
                                UUID uuid = UUID.fromString(file.getName().replace(Config.HOCON_EXT, ""));
                                submitters.put(uuid, new SpongeSubmitter(uuid, cn));
                            }
                            catch (IOException e) {
                                logger.error(Messages.ioException(file));
                            }
                        });
            }
            else {
                logger.info(Messages.failedToReadFiles(dir));
            }
        });
    }

    @Override
    public void save() {
        submitters.forEach((uuid, submitter) -> submitter.save(new File(dir, Config.getHOCONFileName(uuid))));
    }
}
