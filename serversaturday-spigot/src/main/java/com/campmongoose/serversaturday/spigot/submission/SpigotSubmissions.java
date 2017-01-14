package com.campmongoose.serversaturday.spigot.submission;

import com.campmongoose.serversaturday.common.MySQLHandler;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.MySQL;
import com.campmongoose.serversaturday.common.submission.AbstractSubmissions;
import com.campmongoose.serversaturday.spigot.SpigotConfig;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import java.io.File;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SpigotSubmissions extends AbstractSubmissions<Player, SpigotSubmitter>
{
    public SpigotSubmissions()
    {
        super(SpigotServerSaturday.instance().getDataFolder());
    }

    @Nonnull
    @Override
    public SpigotSubmitter getSubmitter(@Nonnull Player player) {
        return submitters.putIfAbsent(player.getUniqueId(), new SpigotSubmitter(player));
    }

    @Override
    public void load()
    {
        Logger logger = SpigotServerSaturday.instance().getLogger();
        if (dir.mkdirs())
            logger.info(Messages.newFile(dir));

        for (File file : dir.listFiles())
        {
            if (!file.getName().endsWith(Config.YAML_EXT))
                continue;

            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            UUID uuid = UUID.fromString(file.getName().replace(Config.YAML_EXT, ""));
            submitters.put(uuid, new SpigotSubmitter(uuid, yaml));
        }
    }

    @Override
    public void save()
    {
        SpigotServerSaturday plugin = SpigotServerSaturday.instance();
        SpigotConfig config = plugin.getPluginConfig();
        MySQLHandler sql = plugin.getMySQLHandler();
        submitters.forEach((uuid, submitter) -> {
            if (config.getSaveFormat().equals(Config.MYSQL) && sql != null)
            {
                try
                {
                    sql.querySQL(MySQL.CREATE_TABLE);
                    sql.querySQL(MySQL.deletePlayer(uuid));
                    for (SpigotBuild build : submitter.getBuilds())
                    {
                        Location location = build.getLocation();
                        sql.querySQL(MySQL.addBuild(submitter.getName(), uuid, build, location.getX(), location.getY(),
                                location.getZ(), location.getYaw(), location.getPitch(), location.getWorld().getName()));
                    }
                }
                catch (ClassNotFoundException | SQLException e)
                {
                    e.printStackTrace();
                }
            }
            else if (config.getSaveFormat().equals(Config.YAML_EXT.replace(".", "")))
                submitter.save(new File(dir, Config.getYAMLFileName(uuid)));
        });
    }
}
