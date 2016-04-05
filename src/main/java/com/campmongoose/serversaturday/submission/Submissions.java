package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.menu.chest.SubmissionsMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Submissions
{
    private final File dir;
    private final Map<UUID, Submitter> submitters = new HashMap<>();

    public Submissions(ServerSaturday plugin)
    {
        this.dir = new File(plugin.getDataFolder(), "submitters");
        load();
    }

    public Submitter getSubmitter(UUID uuid)
    {
        return getSubmitter(Bukkit.getOfflinePlayer(uuid));
    }

    private Submitter getSubmitter(OfflinePlayer player)
    {
        UUID uuid = player.getUniqueId();
        if (!submitters.containsKey(uuid))
            submitters.put(uuid, Submitter.of(player));

        return submitters.get(uuid);
    }

    public List<Submitter> getSubmitters()
    {
        List<Submitter> list = new ArrayList<>();
        list.addAll(submitters.values());
        return list;
    }

    public void openMenu(ServerSaturday plugin, int page, Player player)
    {
        new SubmissionsMenu(plugin, page, Bukkit.createInventory(null, 54, "S.S. Submissions"), player.getUniqueId()).open(player);
    }

    public void newSubmitter(Player player)
    {
        submitters.put(player.getUniqueId(), Submitter.of(player));
    }

    public void load()
    {
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
        //noinspection ConstantConditions
        for (File file : dir.listFiles())
        {
            if (!file.getName().endsWith(".yml"))
                continue;

            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            UUID uuid = UUID.fromString(file.getName().replace(".yml", ""));
            submitters.put(uuid, Submitter.of(uuid, yaml));
        }
    }

    public void save(ServerSaturday plugin)
    {
        for (UUID uuid : submitters.keySet())
            getSubmitter(uuid).save(plugin, new File(dir, uuid.toString() + ".yml"));
    }
}
