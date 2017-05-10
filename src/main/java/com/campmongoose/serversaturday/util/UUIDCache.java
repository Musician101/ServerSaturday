package com.campmongoose.serversaturday.util;

import com.campmongoose.serversaturday.ServerSaturday;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UUIDCache implements Listener {

    private final Map<UUID, String> uuidMap = new HashMap<>();
    private final Map<String, UUID> nameMap = new HashMap<>();

    public UUIDCache() {
        Bukkit.getPluginManager().registerEvents(this, ServerSaturday.instance());
        Bukkit.getOnlinePlayers().forEach(this::add);
        Stream.of(Bukkit.getOfflinePlayers()).forEach(player -> {
            UUID uuid = player.getUniqueId();
            String name = player.getName();
            try {
                name = getCurrentName(player.getUniqueId());
            }
            catch (IOException e) {
                ServerSaturday.instance().getLogger().warning("Could not retrieve up to date name for " + name + " (" + uuid + "). Defaulting to the last name they had on the server.");
            }

            add(uuid, name);
        });
    }

    public void addIfAbsent(UUID uuid, String name) {
        uuidMap.putIfAbsent(uuid, name);
        if (!nameMap.containsValue(uuid)) {
            nameMap.put(name, uuid);
        }
    }

    public void add(Player player) {
        add(player.getUniqueId(), player.getName());
    }

    public void add(UUID uuid) throws IOException {
        add(uuid, getCurrentName(uuid));
    }

    private void add(UUID uuid, String name) {
        uuidMap.put(uuid, name);
        nameMap.put(name, uuid);
    }

    private String getCurrentName(UUID uuid) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "")).openConnection();
        connection.setRequestMethod("GET");
        JsonObject response = new Gson().fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class);
        if (response != null && response.has("name")) {
            return response.get("name").getAsString();
        }

        throw new IllegalArgumentException("A player with uuid " + uuid.toString() + " does not exist.");
    }

    @Nullable
    public String getNameOf(UUID uuid) {
        return uuidMap.get(uuid);
    }

    @Nullable
    public UUID getUUIDOf(String name) {
        return nameMap.get(name);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        add(event.getPlayer());
    }
}
