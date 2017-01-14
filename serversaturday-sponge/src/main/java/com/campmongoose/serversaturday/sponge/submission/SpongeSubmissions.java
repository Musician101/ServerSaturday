package com.campmongoose.serversaturday.sponge.submission;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.AbstractSubmissions;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.annotation.Nonnull;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.Player;

public class SpongeSubmissions extends AbstractSubmissions<Player, SpongeSubmitter> {

    public SpongeSubmissions() {
        super(new File("config/" + Reference.NAME + "/submitters"));
    }

    @Nonnull
    @Override
    public SpongeSubmitter getSubmitter(@Nonnull Player player) {
        return submitters.putIfAbsent(player.getUniqueId(), new SpongeSubmitter(player));
    }

    @Override
    public void load() {
        Logger logger = SpongeServerSaturday.getLogger();
        if (dir.mkdirs()) {
            logger.info(Messages.newFile(dir));
        }

        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.getName().endsWith(Config.HOCON_EXT)) {
                    continue;
                }

                ConfigurationNode cn;
                try {
                    cn = HoconConfigurationLoader.builder().setFile(file).build().load();
                }
                catch (IOException e) {
                    SpongeServerSaturday.getLogger().error(Messages.ioException(file));
                    continue;
                }

                UUID uuid = UUID.fromString(file.getName().replace(Config.HOCON_EXT, ""));
                submitters.put(uuid, new SpongeSubmitter(uuid, cn));
            }
        }
        else {
            logger.info("An error occurred whilst attempting to read the files in " + dir.getName());
        }
    }

    @Override
    public void save() {
        submitters.forEach((key, value) -> value.save(new File(dir, Config.getHOCONFileName(key))));
    }
}
