package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.AbstractConfig;
import com.campmongoose.serversaturday.common.Reference.Config;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

public class SpigotConfig extends AbstractConfig {

    public SpigotConfig() {
        super(new File(SpigotServerSaturday.instance().getDataFolder(), "config.yml"));
        reload();
    }

    @Override
    public void reload() {
        SpigotServerSaturday plugin = SpigotServerSaturday.instance();
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        //Update as default config is changed
        if (!config.contains(Config.CONFIG_VERSION)) {
            config.set(Config.CONFIG_VERSION, 1);
        }

        if (config.getInt(Config.CONFIG_VERSION) == 1) {
            if (!config.contains(Config.MAX_BUILDS)) {
                config.set(Config.MAX_BUILDS, 0);
            }
        }

        maxBuilds = config.getInt(Config.MAX_BUILDS);
    }
}