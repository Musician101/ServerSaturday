package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.AbstractRewardGiver;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.spigot.uuid.MojangAPIException;
import com.campmongoose.serversaturday.spigot.uuid.PlayerNotFoundException;
import com.campmongoose.serversaturday.spigot.uuid.UUIDCacheException;
import com.campmongoose.serversaturday.spigot.gui.chest.RewardsGUI;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class SpigotRewardGiver extends AbstractRewardGiver<PlayerJoinEvent, Player> {

    public SpigotRewardGiver() {
        super(new File(SpigotServerSaturday.instance().getDataFolder(), "rewards_waiting.yml"));
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                SpigotServerSaturday.instance().getLogger().warning(Messages.ioException(file));
                return;
            }
        }

        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        yml.getKeys(false).forEach(key -> {
            UUID uuid = UUID.fromString(key);
            rewardsWaiting.put(uuid, yml.getInt(key + ".amount", 0));
        });
    }

    @Override
    public void givePlayerReward(Player player) {
        UUID uuid = player.getUniqueId();
        int amount = rewardsWaiting.put(uuid, 0);
        for (int i = 0; i < amount; i++) {
            Stream.of(RewardsGUI.getRewards()).filter(Objects::nonNull).forEach(itemStack -> player.getWorld().dropItem(player.getLocation(), itemStack));
        }
    }

    @Override
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        rewardsWaiting.putIfAbsent(uuid, 0);
        if (rewardsWaiting.getOrDefault(uuid, 0) > 0) {
            player.sendMessage(ChatColor.GOLD + Messages.REWARDS_WAITING);
        }
    }

    @Override
    public void save() {
        YamlConfiguration yml = new YamlConfiguration();
        rewardsWaiting.forEach((uuid, amount) -> {
            yml.set(uuid.toString() + ".amount", amount);
            try {
                yml.set(uuid.toString() + ".name", SpigotServerSaturday.instance().getUUIDCache().getNameOf(uuid));
            }
            catch (IOException | MojangAPIException | PlayerNotFoundException | UUIDCacheException e) {
                SpigotServerSaturday.instance().getLogger().warning(e.getMessage().replace(Messages.PREFIX, ""));
            }
        });

        try {
            yml.save(file);
        }
        catch (IOException e) {
            SpigotServerSaturday.instance().getLogger().warning(Messages.ioException(file));
        }
    }
}