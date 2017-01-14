package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.AbstractConfig;
import com.campmongoose.serversaturday.common.MySQLHandler;
import com.campmongoose.serversaturday.common.Reference.Config;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class SpigotConfig extends AbstractConfig
{
    public SpigotConfig()
    {
        super(new File(SpigotServerSaturday.instance().getDataFolder(), "config.yml"));
    }

    @Override
    public void reload()
    {
        SpigotServerSaturday plugin = SpigotServerSaturday.instance();
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        saveFormat = config.getString("save_format", "yml").toLowerCase();
        if (saveFormat.equals(Config.MYSQL))
        {
            ConfigurationSection cs = config.getConfigurationSection("mysql");
            plugin.setMySQLHandler(new MySQLHandler(cs.getString(Config.DATABASE), cs.getString(Config.HOST), cs.getString(Config.PASS), cs.getString(Config.PORT), cs.getString(Config.USER)));
        }
    }
}
