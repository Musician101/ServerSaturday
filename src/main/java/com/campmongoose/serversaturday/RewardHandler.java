package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.Reference.Messages;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

@SuppressWarnings("BlockingMethodInNonBlockingContext")
public final class RewardHandler implements Listener {

    @Nonnull
    private final Map<UUID, Integer> rewards = new HashMap<>();

    public void claimReward(@Nonnull Player player) {
        UUID uuid = player.getUniqueId();
        int amount = rewards.getOrDefault(uuid, 0);
        rewards.put(uuid, 0);
        Server server = Bukkit.getServer();
        IntStream.range(0, amount).forEach(i -> getPlugin().getPluginConfig().getRewards().forEach(command -> server.dispatchCommand(server.getConsoleSender(), command.replace("@p", player.getName()))));
    }

    public void giveReward(@Nonnull OfflinePlayer player) {
        rewards.compute(player.getUniqueId(), (uuid, i) -> i == null ? 1 : ++i);
    }

    public void load() {
        ServerSaturday plugin = getPlugin();
        Path path = plugin.getDataFolder().toPath().resolve("rewards.yml");
        try {
            Files.createFile(path);
            YamlConfiguration rewards = YamlConfiguration.loadConfiguration(path.toFile());
            rewards.getKeys(false).forEach(key -> this.rewards.put(UUID.fromString(key), rewards.getInt(key)));
        }
        catch (Exception e) {
            plugin.getSLF4JLogger().error(Messages.failedToReadFile(path.toFile()), e);
        }
    }

    @EventHandler
    public void onJoin(@Nonnull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (rewards.getOrDefault(uuid, 0) > 0) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> player.sendMessage(Messages.REWARDS_WAITING), 20L);
        }
    }

    public void save() {
        ServerSaturday plugin = getPlugin();
        Path path = plugin.getDataFolder().toPath().resolve("rewards.yml");
        try {
            Files.createFile(path);
            YamlConfiguration rewards = new YamlConfiguration();
            this.rewards.forEach((uuid, i) -> rewards.set(uuid.toString(), i));
            rewards.save(path.toFile());
        }
        catch (Exception e) {
            plugin.getSLF4JLogger().error(Messages.failedToWriteFile(path), e);
        }
    }
}
