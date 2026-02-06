package com.campmongoose.serversaturday;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.util.Types;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

@NullMarked
public final class RewardHandler implements Listener {

    private final Map<UUID, Integer> rewards = new HashMap<>();

    public void claimReward(Player player) {
        UUID uuid = player.getUniqueId();
        int amount = rewards.getOrDefault(uuid, 0);
        rewards.put(uuid, 0);
        Server server = Bukkit.getServer();
        IntStream.range(0, amount).forEach(i -> getPlugin().getConfig().getStringList("rewards").forEach(command -> server.dispatchCommand(server.getConsoleSender(), command.replace("@p", player.getName()))));
    }

    public void giveReward(OfflinePlayer player) {
        rewards.compute(player.getUniqueId(), (uuid, i) -> i == null ? 1 : ++i);
    }

    public void load() {
        Path path = getPlugin().getDataFolder().toPath().resolve("rewards.yml");
        try {
            if (Files.notExists(path)) {
                Files.createFile(path);
            }

            YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(path).nodeStyle(NodeStyle.BLOCK).build();
            ConfigurationNode node = loader.load();
            rewards.putAll(node.require(Types.makeMap(UUID.class, Integer.class)));
        }
        catch (Exception e) {
            getPlugin().getSLF4JLogger().error("Failed to read " + path.getFileName(), e);
        }
    }

    private YamlConfigurationLoader loader() {
        Path path = getPlugin().getDataFolder().toPath().resolve("rewards.yml");
        return YamlConfigurationLoader.builder().path(path).nodeStyle(NodeStyle.BLOCK).build();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (rewards.getOrDefault(uuid, 0) > 0) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> player.sendMessage(Messages.REWARDS_WAITING), 20L);
        }
    }

    public void save() {
        Path path = getPlugin().getDataFolder().toPath().resolve("rewards.yml");
        try {
            if (Files.notExists(path)) {
                Files.createFile(path);
            }

            YamlConfigurationLoader loader = loader();
            ConfigurationNode node = loader.createNode();
            node.set(rewards.entrySet().stream().filter(e -> e.getValue() > 0).collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
            loader.save(node);
        }
        catch (Exception e) {
            getPlugin().getSLF4JLogger().error("Failed to write " + path.getFileName(), e);
        }
    }
}
