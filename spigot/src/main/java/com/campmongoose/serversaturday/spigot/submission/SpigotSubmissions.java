package com.campmongoose.serversaturday.spigot.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.submission.AbstractSubmissions;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.menu.chest.SubmissionsMenu;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class SpigotSubmissions extends AbstractSubmissions<SpigotSubmitter>
{
    public SpigotSubmissions()
    {
        super(SpigotServerSaturday.getInstance().getDataFolder());
    }

    @Override
    public SpigotSubmitter getSubmitter(UUID uuid)
    {
        if (!submitters.containsKey(uuid))
            submitters.put(uuid, SpigotSubmitter.of(Bukkit.getOfflinePlayer(uuid)));

        return submitters.get(uuid);
    }

    @Override
    public void openMenu(int page, UUID uuid)
    {
        new SubmissionsMenu(page, Bukkit.createInventory(null, 54, MenuText.SUBMISSIONS), uuid).open(uuid);
    }

    @Override
    public void load()
    {
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
        //noinspection ConstantConditions
        for (File file : dir.listFiles())
        {
            if (!file.getName().endsWith(Config.YAML_EXT))
                continue;

            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            UUID uuid = UUID.fromString(file.getName().replace(Config.YAML_EXT, ""));
            submitters.put(uuid, SpigotSubmitter.of(uuid, yaml));
        }
    }

    @Override
    public void save()
    {
        for (UUID uuid : submitters.keySet())
            getSubmitter(uuid).save(new File(dir, Config.getYAMLFileName(uuid)));
    }
}
