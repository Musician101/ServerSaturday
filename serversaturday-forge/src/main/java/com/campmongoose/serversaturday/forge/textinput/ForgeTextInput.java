package com.campmongoose.serversaturday.forge.textinput;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

//TODO replace with GUI
@Deprecated
public class ForgeTextInput implements Listener {

    private static final Map<UUID, BiConsumer<ServerPlayerEntity, String>> PLAYERS = new HashMap<>();

    public static void addPlayer(ServerPlayerEntity player, BiConsumer<ServerPlayerEntity, String> action) {
        PLAYERS.put(player.getUniqueID(), action);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        ServerPlayerEntity player = event.getPlayer();
        if (!PLAYERS.containsKey(player.getUniqueID())) {
            return;
        }

        event.setCancelled(true);
        PLAYERS.remove(player.getUniqueID()).accept(player, event.getMessage());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        PLAYERS.remove(event.getPlayer().getUniqueID());
    }
}
