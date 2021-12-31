package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.Reference.Messages;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class RewardHandler implements Listener {

    @Nonnull
    private final Map<UUID, Integer> rewards = new HashMap<>();

    public void claimReward(@Nonnull Player player) {
        UUID uuid = player.getUniqueId();
        int amount = rewards.getOrDefault(uuid, 0);
        rewards.put(uuid, 0);
        ServerSaturday plugin = ServerSaturday.getInstance();
        Server server = plugin.getServer();
        IntStream.range(0, amount).forEach(i -> plugin.getPluginConfig().getRewards().forEach(command -> server.dispatchCommand(server.getConsoleSender(), command.replace("@p", player.getName()))));
    }

    public void giveReward(@Nonnull OfflinePlayer player) {
        rewards.compute(player.getUniqueId(), (uuid, i) -> i == null ? 1 : ++i);
    }

    public void load() {
        ServerSaturday plugin = ServerSaturday.getInstance();
        File file = new File(plugin.getDataFolder(), "rewards.yml");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            YamlConfiguration rewards = YamlConfiguration.loadConfiguration(file);
            rewards.getKeys(false).forEach(key -> this.rewards.put(UUID.fromString(key), rewards.getInt(key)));
        }
        catch (Exception e) {
            plugin.getLog4JLogger().error(Messages.failedToReadFile(file), e);
        }
    }

    @EventHandler
    public void onJoin(@Nonnull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (rewards.getOrDefault(uuid, 0) > 0) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(ServerSaturday.getInstance(), () -> player.sendMessage(Component.text(Messages.REWARDS_WAITING).color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand("/ss claim"))), 20L);
        }
    }

    public void save() {
        ServerSaturday plugin = ServerSaturday.getInstance();
        File file = new File(plugin.getDataFolder(), "rewards.yml");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            YamlConfiguration rewards = new YamlConfiguration();
            this.rewards.forEach((uuid, i) -> rewards.set(uuid.toString(), i));
            rewards.save(file);
        }
        catch (Exception e) {
            plugin.getLog4JLogger().error(Messages.failedToWriteFile(file), e);
        }
    }
}
