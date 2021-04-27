package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.RewardGiver;
import io.musician101.musicianlibrary.java.configurate.ConfigurateLoader;
import java.io.File;
import java.util.UUID;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SpigotRewardGiver extends RewardGiver<PlayerJoinEvent, Player> implements Listener {

    public SpigotRewardGiver() {
        super(new File(SpigotServerSaturday.instance().getDataFolder(), "rewards_waiting.yml"), ConfigurateLoader.YAML);
        Bukkit.getPluginManager().registerEvents(this, SpigotServerSaturday.instance());
    }

    @Override
    protected void reportError() {
        SpigotServerSaturday.instance().getLogger().warning(Messages.CONFIG_READ_ERROR);
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
}
