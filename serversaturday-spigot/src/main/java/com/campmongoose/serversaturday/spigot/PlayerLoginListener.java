package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.spigot.uuid.UUIDCacheException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        SpigotServerSaturday plugin = SpigotServerSaturday.instance();
        try {
            plugin.getUUIDCache().add(player.getUniqueId(), player.getName());
        }
        catch (UUIDCacheException e) {
            plugin.getLogger().info(Messages.playerJoinAddFail(player.getName(), player.getUniqueId()));
        }
    }
}
