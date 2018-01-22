package com.campmongoose.serversaturday.sponge.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.common.submission.Submissions;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter.SpongeSerializer;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SpongeSubmissions extends Submissions<Player, SpongeSubmitter> {

    public SpongeSubmissions(@Nonnull File storageDir) {
        super(storageDir, new GsonBuilder().setPrettyPrinting().registerTypeAdapter(SpongeBuild.class, new SpongeBuild.SpongeSerializer()).registerTypeAdapter(SpongeSubmitter.class, new SpongeSerializer()).registerTypeAdapter(new TypeToken<Location<World>>(){}.getType(), new LocationSerializer()).create());
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
            dir.mkdirs();
            File[] files = dir.listFiles();
            if (files != null) {
                Stream.of(files).filter(file -> file.getName().endsWith(Config.HOCON_EXT))
                        .forEach(file -> {
                            try {
                                SpongeSubmitter submitter = gson.fromJson(new FileReader(file), SpongeSubmitter.class);
                                submitters.put(submitter.getUUID(), submitter);
                            }
                            catch (IOException e) {
                                logger.error(Messages.failedToReadFile(file));
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
        submitters.forEach((uuid, submitter) -> {
            File file = new File(dir, Config.getFileName(uuid));
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

                OutputStream os = new FileOutputStream(file);
                os.write(gson.toJson(submitter).getBytes());
                os.close();
            }
            catch (IOException e) {
                SpongeServerSaturday.instance().map(ServerSaturday::getLogger).ifPresent(logger -> logger.error(Messages.failedToWriteFile(file)));
            }
        });
    }
}
