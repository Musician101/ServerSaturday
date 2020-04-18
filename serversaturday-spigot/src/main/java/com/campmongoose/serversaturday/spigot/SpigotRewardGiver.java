package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.RewardGiver;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SpigotRewardGiver extends RewardGiver<PlayerJoinEvent, Player> implements Listener {

    public SpigotRewardGiver() {
        super(new File(SpigotServerSaturday.instance().getDataFolder(), "rewards_waiting.yml"));
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                SpigotServerSaturday.instance().getLogger().warning(Messages.failedToReadFile(file));
                return;
            }
        }

        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        yml.getKeys(false).forEach(key -> {
            UUID uuid = UUID.fromString(key);
            rewardsWaiting.put(uuid, yml.getInt(key + ".amount", 0));
        });
        Bukkit.getPluginManager().registerEvents(this, SpigotServerSaturday.instance());
    }

    @Override
    public void givePlayerReward(@Nonnull Player player) {
        UUID uuid = player.getUniqueId();
        int amount = rewardsWaiting.getOrDefault(uuid, 0);
        rewardsWaiting.put(uuid, 0);
        SpigotServerSaturday plugin = SpigotServerSaturday.instance();
        Server server = plugin.getServer();
        IntStream.range(0, amount).forEach(i -> plugin.getPluginConfig().getRewards().forEach(command -> server.dispatchCommand(server.getConsoleSender(), command.replace("@p", player.getName()))));
    }

    @EventHandler
    @Override
    public void onJoin(@Nonnull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (rewardsWaiting.getOrDefault(uuid, 0) > 0) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotServerSaturday.instance(), () -> player.sendMessage(ChatColor.GOLD + Messages.REWARDS_WAITING), 20L);
        }
    }

    @Override
    public void save() {
        YamlConfiguration yml = new YamlConfiguration();
        rewardsWaiting.forEach((uuid, amount) -> {
            yml.set(uuid.toString() + ".amount", amount);
            SpigotSubmitter submitter = SpigotServerSaturday.instance().getSubmissions().getSubmitter(uuid);
            if (submitter != null) {
                yml.set(uuid.toString() + ".name", submitter.getName());
            }
            else {
                yml.set(uuid.toString() + ".name", "Invalid UUID?");
            }
        });

        try {
            yml.save(file);
        }
        catch (IOException e) {
            SpigotServerSaturday.instance().getLogger().warning(Messages.failedToWriteFile(file));
        }
    }
}
