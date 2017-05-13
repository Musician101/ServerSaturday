package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.menu.chest.SubmissionsMenu;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Submissions {

    private boolean hasLoaded = false;
    private final File dir;
    private final Map<UUID, Submitter> submitters = new HashMap<>();

    public Submissions() {
        this.dir = new File(ServerSaturday.instance().getDataFolder(), "submitters");
        load();
    }

    public Submitter getSubmitter(UUID uuid) {
        submitters.putIfAbsent(uuid, Submitter.of(Bukkit.getPlayer(uuid)));
        return submitters.get(uuid);
    }

    public List<Submitter> getSubmitters() {
        List<Submitter> list = new ArrayList<>();
        list.addAll(submitters.values());
        return list;
    }

    public boolean hasLoaded() {
        return hasLoaded;
    }

    public void load() {
        dir.mkdirs();
        for (File file : dir.listFiles()) {
            if (!file.getName().endsWith(".yml")) {
                continue;
            }

            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            UUID uuid = UUID.fromString(file.getName().replace(".yml", ""));
            submitters.put(uuid, Submitter.of(uuid, yaml));
        }

        hasLoaded = true;
    }

    public void openMenu(int page, Player player) {
        new SubmissionsMenu(page, Bukkit.createInventory(null, 54, "S.S. Submissions"), player.getUniqueId()).open(player);
    }

    public void save() {
        submitters.forEach((uuid, submitter) -> submitter.save(new File(dir, uuid.toString() + ".yml")));
    }
}
