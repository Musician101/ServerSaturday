package com.campmongoose.serversaturday.util;

import com.campmongoose.serversaturday.ServerSaturday;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
        List<UUID> uuids = Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toList());
        Stream.of(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getUniqueId).forEach(uuids::add);
        uuids.forEach(uuid -> {
            try {
                add(uuid, getCurrentName(uuid));
            }
            catch (IOException e) {
                ioException(uuid, Bukkit.getOfflinePlayer(uuid).getName());
            }
        });
    }

    private String getCurrentName(UUID uuid) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("https://api.mojang.com/user/profiles/" + uuid.toString().replace("-", "") + "/names").openConnection();
        JsonArray response = new Gson().fromJson(new InputStreamReader(connection.getInputStream()), JsonArray.class);
        String name = null;
        JsonObject jsonObject = null;
        if (response.size() == 1) {
            jsonObject = response.get(0).getAsJsonObject();
            name = jsonObject.has("name") ? jsonObject.get("name").getAsString() : null;
        }
        else {
            for (JsonElement jsonElement : response) {
                JsonObject jo = jsonElement.getAsJsonObject();
                if (jo.getAsJsonObject().has("changedToAt")) {
                    jsonObject = jo;
                }
            }

            if (jsonObject != null) {
                name = jsonObject.get("name").getAsString();
            }
        }

        if (name != null) {
            return name;
        }

        throw new IllegalArgumentException("A player with uuid " + uuid.toString() + " does not exist.");
    }

    public void add(Player player) {
        uuidMap.put(player.getUniqueId(), player.getName());
    }

    private void add(UUID uuid, String name) {
        uuidMap.put(uuid, name);
    }

    public void addIfAbsent(UUID uuid, String name) {
        try {
            uuidMap.putIfAbsent(uuid, getCurrentName(uuid));
        }
        catch (IOException e) {
            ioException(uuid, name);
        }
    }

    private void ioException(UUID uuid, String name) {
        ServerSaturday.instance().getLogger().warning("Could not retrieve up to date name for " + name + " (" + uuid + "). Defaulting to the last name they had on the server.");
        add(uuid, name);
    }

    public String getNameOf(UUID uuid) {
        return uuidMap.get(uuid);
    }

    public UUID getUUIDOf(String name) {
        return uuidMap.inverse().get(name);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        add(event.getPlayer());
    }
}
