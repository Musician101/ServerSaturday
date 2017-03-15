package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.AbstractConfig;
import com.campmongoose.serversaturday.common.Reference.Config;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

public class SpigotConfig extends AbstractConfig {

    public SpigotConfig() {
        super(new File(SpigotServerSaturday.instance().getDataFolder(), "config.yml"));
    }

    @Override
    public void reload() {
        SpigotServerSaturday plugin = SpigotServerSaturday.instance();
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        maxBuilds = config.getInt(Config.MAX_BUILDS, 0);
    }
}
