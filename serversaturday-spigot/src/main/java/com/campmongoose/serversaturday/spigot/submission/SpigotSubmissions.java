package com.campmongoose.serversaturday.spigot.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Submissions;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter.SpigotSerializer;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SpigotSubmissions extends Submissions<Player, SpigotSubmitter> {

    public SpigotSubmissions() {
        super(SpigotServerSaturday.instance().getDataFolder(), new GsonBuilder().setPrettyPrinting().registerTypeAdapter(SpigotBuild.class, new SpigotBuild.SpigotSerializer()).registerTypeAdapter(SpigotSubmitter.class, new SpigotSerializer()).registerTypeAdapter(Location.class, new LocationSerializer()).create());
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
        dir.mkdirs();
        File[] files = dir.listFiles();
        if (files != null) {
            Stream.of(files).filter(file -> file.getName().endsWith(Config.JSON))
                    .forEach(file -> {
                        try(FileReader fr = new FileReader(file)) {
                            SpigotSubmitter submitter = gson.fromJson(fr, SpigotSubmitter.class);
                            if (!submitter.getBuilds().isEmpty()) {
                                submitters.put(submitter.getUUID(), submitter);
                            }
                        }
                        catch (IOException e) {
                            logger.warning(Messages.failedToReadFile(file));
                        }
                    });
        }
        else {
            logger.info(Messages.failedToReadFiles(dir));
        }
    }

    @Override
    public void save() {
        submitters.forEach((uuid, submitter) -> {
            File file = new File(dir, Config.getFileName(uuid));
            if (!submitter.getBuilds().isEmpty()) {
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    OutputStream os = new FileOutputStream(file);
                    os.write(gson.toJson(submitter).getBytes());
                    os.close();
                }
                catch (IOException e) {
                    SpigotServerSaturday.instance().getLogger().warning(Messages.failedToWriteFile(file));
                }
            }
            else {
                file.delete();
            }
        });
    }
}
