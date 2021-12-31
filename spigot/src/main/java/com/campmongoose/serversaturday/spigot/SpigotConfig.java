package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.AbstractConfig;
import com.campmongoose.serversaturday.common.Reference.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class SpigotConfig extends AbstractConfig {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void reload() {
        SpigotServerSaturday plugin = SpigotServerSaturday.instance();
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        maxBuilds = config.getInt(Config.MAX_BUILDS);
        rewards.clear();
        rewards.addAll(config.getStringList(Config.REWARDS));
        databaseOptions.clear();
        config.getConfigurationSection(Config.DATABASE).getValues(false).forEach((k, v) -> databaseOptions.put(k, v.toString()));
    }
}
