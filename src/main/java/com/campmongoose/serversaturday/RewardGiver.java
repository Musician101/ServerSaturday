package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.menu.RewardsMenu;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class RewardGiver implements Listener {

    private final File file;
    private final Map<UUID, Integer> rewardsWaiting = new HashMap<>();
    private final Map<UUID, String> names = new HashMap<>();

    public RewardGiver() {
        ServerSaturday plugin = ServerSaturday.instance();
        file = new File(plugin.getDataFolder(), "rewards_waiting.yaml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                plugin.getLogger().warning("An error occurred while trying to load up rewards_waiting.yml");
                return;
            }
        }

        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        Bukkit.getOnlinePlayers().forEach(player -> {
            UUID uuid = player.getUniqueId();
            rewardsWaiting.put(uuid, yml.getInt(uuid.toString() + ".amount"));
            names.put(uuid, player.getName());
        });

        Stream.of(Bukkit.getOfflinePlayers()).forEach(player -> {
            UUID uuid = player.getUniqueId();
            rewardsWaiting.put(uuid, yml.getInt(uuid.toString() + ".amount"));
            String name = plugin.getUUIDCache().getNameOf(uuid);
            if (name == null) {
                name = yml.getString(uuid.toString() + ".name");
            }
            names.put(uuid, name);
        });

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void addReward(UUID uuid) {
        rewardsWaiting.compute(uuid, (id, amount) -> ++amount);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.sendMessage(ChatColor.GOLD + Reference.PREFIX + "Hey, you! You have rewards waiting for you. Claim them with /ss getrewards");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        rewardsWaiting.putIfAbsent(uuid, 0);
        if (rewardsWaiting.getOrDefault(uuid, 0) > 0) {
            player.sendMessage(ChatColor.GOLD + Reference.PREFIX + "Hey, you! You have rewards waiting for you. Claim them with /ss getrewards");
        }
    }

    public void givePlayerReward(Player player) {
        UUID uuid = player.getUniqueId();
        for (int i = 0; i < rewardsWaiting.get(uuid); i++) {
            Stream.of(RewardsMenu.getRewards()).filter(Objects::nonNull).forEach(itemStack -> player.getWorld().dropItem(player.getLocation(), itemStack));
        }

        rewardsWaiting.put(uuid, 0);
    }

    public void save() {
        YamlConfiguration yml = new YamlConfiguration();
        rewardsWaiting.forEach((uuid, amount) -> {
            yml.set(uuid.toString() + ".amount", amount);
            yml.set(uuid.toString() + ".name", names.get(uuid));
        });

        try {
            yml.save(file);
        }
        catch (IOException e) {
            ServerSaturday.instance().getLogger().warning("Could not save rewards_waiting.yml");
        }
    }
}
