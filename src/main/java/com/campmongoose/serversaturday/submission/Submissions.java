package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.menu.chest.SubmissionsMenu;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.campmongoose.serversaturday.json.JsonUtils.GSON;

public class Submissions {

    private final File dir;
    private final Map<UUID, Submitter> submitters = new HashMap<>();

    public Submissions() {
        this.dir = new File(ServerSaturday.instance().getDataFolder(), "submitters");
        load();
    }

    @Nullable
    public Submitter getSubmitter(UUID uuid) {
        return submitters.get(uuid);
    }

    public List<Submitter> getSubmitters() {
        return new ArrayList<>(submitters.values());
    }

    public void load() {
        dir.mkdirs();
        for (File file : dir.listFiles()) {
            if (!file.getName().endsWith(".json")) {
                continue;
            }

            try {
                Submitter submitter = GSON.fromJson(new FileReader(file), Submitter.class);
                submitters.put(submitter.getUUID(), submitter);
            }
            catch (FileNotFoundException e) {
                ServerSaturday.instance().getLogger().severe("An error occurred while loading " + file.getName());
            }
        }
    }

    public void openMenu(int page, Player player) {
        new SubmissionsMenu(page, Bukkit.createInventory(null, 54, "S.S. Submissions"), player.getUniqueId()).open(player);
    }

    public void save() {
        submitters.forEach((uuid, submitter) -> {
            File file = new File(dir, uuid.toString() + ".json");
            if (!file.exists()) {
                dir.mkdirs();
                try {
                    file.createNewFile();
                }
                catch (IOException e) {
                    ServerSaturday.instance().getLogger().severe("An error occurred while saving " + file.getName());
                    return;
                }
            }

            try {
                OutputStream os = new FileOutputStream(file);
                os.write(GSON.toJson(submitter).getBytes());
                os.close();
            }
            catch (IOException e) {
                ServerSaturday.instance().getLogger().severe("An error occurred while saving " + file.getName());
            }
        });
    }

    public void updateSubmitterName(Player player) {
        Submitter submitter = submitters.getOrDefault(player.getUniqueId(), new Submitter(player));
        submitter.setName(player.getName());
        submitters.put(submitter.getUUID(), submitter);
    }
}
