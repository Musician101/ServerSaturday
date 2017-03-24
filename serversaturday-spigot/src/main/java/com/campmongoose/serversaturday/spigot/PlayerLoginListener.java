package com.campmongoose.serversaturday.spigot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {


    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        SpigotServerSaturday.instance().getUUIDCache().add(player.getUniqueId(), player.getName());
    }
}
