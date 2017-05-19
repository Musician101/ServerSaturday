package com.campmongoose.serversaturday.spigot.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.AbstractSubmissions;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SpigotSubmissions extends AbstractSubmissions<Player, SpigotSubmitter> {

    public SpigotSubmissions() {
        super(SpigotServerSaturday.instance().getDataFolder());
    }

    @Nonnull
    @Override
    public SpigotSubmitter getSubmitter(@Nonnull Player player) {
        submitters.putIfAbsent(player.getUniqueId(), new SpigotSubmitter(player));
        return submitters.get(player.getUniqueId());
    }

    @Override
    public void load() {
        Logger logger = SpigotServerSaturday.instance().getLogger();
        if (dir.mkdirs()) {
            logger.info(Messages.newFile(dir));
        }

        File[] files = dir.listFiles();
        if (files != null) {
            Stream.of(files).filter(file -> file.getName().endsWith(Config.YAML_EXT))
                    .forEach(file -> {
                        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                        UUID uuid = UUID.fromString(file.getName().replace(Config.YAML_EXT, ""));
                        submitters.put(uuid, new SpigotSubmitter(uuid, yaml));
                    });
        }
        else {
            logger.info("An error occurred whilst attempting to read the files in " + dir.getName());
        }

        super.load();
    }

    @Override
    public void save() {
        submitters.forEach((uuid, submitter) -> submitter.save(new File(dir, Config.getYAMLFileName(uuid))));
    }
}
