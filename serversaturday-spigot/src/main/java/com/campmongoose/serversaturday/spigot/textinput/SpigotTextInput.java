package com.campmongoose.serversaturday.spigot.textinput;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SpigotTextInput implements Listener {

    private static final Map<UUID, BiConsumer<Player, String>> PLAYERS = new HashMap<>();

    public static void addPlayer(Player player, BiConsumer<Player, String> action) {
        PLAYERS.put(player.getUniqueId(), action);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!PLAYERS.containsKey(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
        PLAYERS.remove(player.getUniqueId()).accept(player, event.getMessage());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        PLAYERS.remove(event.getPlayer().getUniqueId());
    }
}
