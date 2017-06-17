package com.campmongoose.serversaturday.util;

import com.campmongoose.serversaturday.ServerSaturday;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.util.UUIDTypeAdapter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UUIDCache implements Listener {

    private final BiMap<UUID, String> uuidMap = HashBiMap.create();

    public UUIDCache() {
        Bukkit.getPluginManager().registerEvents(this, ServerSaturday.instance());
        Bukkit.getOnlinePlayers().forEach(this::add);
        Stream.of(Bukkit.getOfflinePlayers()).forEach(this::add);
        Bukkit.getOnlinePlayers().forEach(this::add);
    }

    public void addIfAbsent(@Nonnull UUID uuid, @Nonnull String name) {
        uuidMap.putIfAbsent(uuid, name);
    }

    public void add(@Nonnull OfflinePlayer player) {
        add(player.getUniqueId(), player.getName());
    }

    private void add(@Nonnull UUID uuid, @Nonnull String name) {
        uuidMap.put(uuid, name);
    }

    @Nullable
    private JsonObject get(@Nonnull String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        return new Gson().fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class);
    }

    @Nonnull
    private String getCurrentName(@Nonnull UUID uuid) throws IOException, MojangAPIException, PlayerNotFoundException {
        JsonObject response = get(UUIDTypeAdapter.fromUUID(uuid));
        if (response != null) {
            if (response.has("name")) {
                return response.get("name").getAsString();
            }

            throw new PlayerNotFoundException(uuid);
        }

        throw new MojangAPIException();
    }

    @Nonnull
    private UUID getCurrentUUID(@Nonnull String name) throws IOException, MojangAPIException, PlayerNotFoundException {
        JsonObject response = get("https://api.mojang.com/users/profiles/minecraft/" + name);
        if (response != null) {
            if (response.has("id")) {
                return UUIDTypeAdapter.fromString(response.get("id").getAsString());
            }

            throw new PlayerNotFoundException(name);
        }

        throw new MojangAPIException();
    }

    @Nonnull
    public String getNameOf(@Nonnull UUID uuid) throws IOException, MojangAPIException, PlayerNotFoundException {
        if (uuidMap.containsKey(uuid)) {
            return uuidMap.get(uuid);
        }

        add(uuid, getCurrentName(uuid));
        return uuidMap.get(uuid);
    }

    @Nonnull
    public UUID getUUIDOf(@Nonnull String name) throws IOException, MojangAPIException, PlayerNotFoundException {
        BiMap<String, UUID> nameMap = uuidMap.inverse();
        if (nameMap.containsKey(name)) {
            return nameMap.get(name);
        }

        add(getCurrentUUID(name), name);
        return uuidMap.inverse().get(name);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        add(event.getPlayer());
    }
}
