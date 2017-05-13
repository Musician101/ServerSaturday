package com.campmongoose.serversaturday.util;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.ServerSaturday;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UUIDCache implements Listener {

    private final BiMap<UUID, String> uuidMap = HashBiMap.create();

    public UUIDCache() {
        Bukkit.getPluginManager().registerEvents(this, ServerSaturday.instance());
        Bukkit.getOnlinePlayers().forEach(this::add);
        List<UUID> uuids = Stream.of(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getUniqueId).collect(Collectors.toList());
        init(uuids);
    }

    private void init(List<UUID> uuids) {
        int requestLimit = 0;
        for (UUID uuid : uuids) {
            if (requestLimit >= 100) {
                try {
                    Thread.sleep(100L);
                }
                catch (InterruptedException e) {
                    ServerSaturday.instance().getLogger().warning("Someone woke me up from my nap early. Some players might not have up to date names.");
                }

                requestLimit = 0;
            }

            try {
                add(uuid);
            }
            catch(PlayerNotFoundException e) {
                ServerSaturday.instance().getLogger().warning(e.getMessage().replace(Reference.PREFIX, ""));
            }
            catch(IOException e) {
                String name = Bukkit.getOfflinePlayer(uuid).getName();
                ServerSaturday.instance().getLogger().warning("Could not retrieve up to date name for " + name + " (" + uuid + "). Defaulting to the last name they had on the server.");
                add(uuid, name);
            }

            requestLimit++;
        }
    }

    public void addIfAbsent(UUID uuid, String name) {
        uuidMap.putIfAbsent(uuid, name);
    }

    public void add(Player player) {
        add(player.getUniqueId(), player.getName());
    }

    public void add(UUID uuid) throws IOException, PlayerNotFoundException {
        add(uuid, getCurrentName(uuid));
    }

    private void add(UUID uuid, String name) {
        uuidMap.put(uuid, name);
    }

    private String getCurrentName(UUID uuid) throws IOException, PlayerNotFoundException {
        HttpURLConnection connection = (HttpURLConnection) new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "")).openConnection();
        connection.setRequestMethod("GET");
        JsonObject response = new Gson().fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class);
        if (response != null && response.has("name")) {
            return response.get("name").getAsString();
        }

        throw new PlayerNotFoundException(uuid);
    }

    @Nullable
    public String getNameOf(UUID uuid) {
        return uuidMap.get(uuid);
    }

    @Nullable
    public UUID getUUIDOf(String name) {
        return uuidMap.inverse().get(name);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        add(event.getPlayer());
    }
}
