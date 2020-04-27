package com.campmongoose.serversaturday.forge;

import com.campmongoose.serversaturday.common.AbstractConfig;
import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.logging.log4j.Logger;

public class ForgeConfig extends AbstractConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public ForgeConfig() {
        super(new File("config/" + Reference.ID, "config.json"));
        reload();
    }

    @Override
    public void reload() {
        Logger logger = ForgeServerSaturday.LOGGER;
        try {
            JsonArray rewards = new JsonArray();
            rewards.add("tell @p Quack \\_o<");
            if (!configFile.exists()) {
                logger.info("Generating default config file...");
                configFile.createNewFile();
                JsonObject config = new JsonObject();
                config.addProperty(Config.MAX_BUILDS, 0);
                config.add(Config.REWARDS, rewards);
                FileWriter fw = new FileWriter(configFile);
                GSON.toJson(config, fw);
                logger.info("Default file created.");
            }

            FileReader fr = new FileReader(configFile);
            JsonObject config = GSON.fromJson(fr, JsonObject.class);
            if (config.has(Config.MAX_BUILDS)) {
                config.addProperty(Config.MAX_BUILDS, 0);
            }

            if (config.has(Config.REWARDS)) {
                config.add(Config.REWARDS, rewards);
            }

            FileWriter fw = new FileWriter(configFile);
            GSON.toJson(config, fw);
            maxBuilds = config.get(Config.MAX_BUILDS).getAsInt();
            this.rewards.clear();
            this.rewards.addAll(StreamSupport.stream(config.getAsJsonArray(Config.REWARDS).spliterator(), false).map(JsonElement::getAsString).collect(Collectors.toList()));
        }
        catch (IOException e) {
            logger.info(Messages.failedToReadFile(configFile));
        }
    }
}
