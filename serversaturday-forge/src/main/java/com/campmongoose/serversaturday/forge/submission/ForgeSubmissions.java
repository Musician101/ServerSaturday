package com.campmongoose.serversaturday.forge.submission;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Submissions;
import com.campmongoose.serversaturday.forge.ForgeServerSaturday;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.apache.logging.log4j.Logger;

public class ForgeSubmissions extends Submissions<ServerPlayerEntity, ForgeSubmitter> {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ForgeBuild.class, new ForgeBuild.Serializer()).registerTypeAdapter(ForgeSubmitter.class, new ForgeSubmitter.Serializer()).registerTypeAdapter(Location.class, new Location.Serializer()).create();

    public ForgeSubmissions() {
        super(new File("config/" + Reference.ID, "submissions"), GSON);
    }

    @Nonnull
    @Override
    public ForgeSubmitter getSubmitter(@Nonnull ServerPlayerEntity player) {
        submitters.putIfAbsent(player.getUniqueID(), new ForgeSubmitter(player));
        return submitters.get(player.getUniqueID());
    }

    @Override
    public void load() {
        Logger logger = ForgeServerSaturday.LOGGER;
        dir.mkdirs();
        File[] files = dir.listFiles();
        if (files != null) {
            Stream.of(files).filter(file -> file.getName().endsWith(Config.JSON)).forEach(file -> {
                try (FileReader fr = new FileReader(file)) {
                    ForgeSubmitter submitter = gson.fromJson(fr, ForgeSubmitter.class);
                    if (!submitter.getBuilds().isEmpty()) {
                        submitters.put(submitter.getUUID(), submitter);
                    }
                }
                catch (IOException e) {
                    logger.warn(Messages.failedToReadFile(file));
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
                    ForgeServerSaturday.LOGGER.warn(Messages.failedToWriteFile(file));
                }
            }
            else {
                file.delete();
            }
        });
    }
}
