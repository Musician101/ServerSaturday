package com.campmongoose.serversaturday.spigot.uuid;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UUIDCache {

    private final BiMap<UUID, String> uuidMap = HashBiMap.create();

    public UUIDCache() {

    }

    public void add(@Nonnull UUID uuid, @Nonnull String name) {
        uuidMap.put(uuid, name);
    }

    public void add(@Nonnull UUID uuid) throws IOException, MojangAPIException, PlayerNotFoundException {
        add(uuid, getCurrentNameOf(uuid));
    }

    public void addIfAbsent(@Nonnull UUID uuid, @Nonnull String name) {
        try {
            uuidMap.putIfAbsent(uuid, getCurrentNameOf(uuid));
        }
        catch (IOException | MojangAPIException | PlayerNotFoundException e) {
            uuidMap.putIfAbsent(uuid, name);
        }
    }

    @Nullable
    private JsonObject get(@Nonnull String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        return new Gson().fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class);
    }

    @Nonnull
    private String getCurrentNameOf(@Nonnull UUID uuid) throws IOException, MojangAPIException, PlayerNotFoundException {
        JsonObject response = get("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", ""));
        if (response != null) {
            if (response.has("name")) {
                return response.get("name").getAsString();
            }

            throw new PlayerNotFoundException(uuid);
        }

        throw new MojangAPIException();
    }

    @Nonnull
    private UUID getCurrentUUIDOf(@Nonnull String name) throws IOException, MojangAPIException, PlayerNotFoundException {
        JsonObject response = get("https://api.mojang.com/users/profiles/minecraft/" + name);
        if (response != null) {
            if (response.has("id")) {
                return UUID.fromString(response.get("id").getAsString().replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
            }

            throw new PlayerNotFoundException(name);
        }

        throw new MojangAPIException();
    }

    @Nullable
    public String getNameOf(@Nonnull UUID uuid) throws MojangAPIException, IOException, PlayerNotFoundException {
        if (!uuidMap.containsKey(uuid)) {
            add(uuid, getCurrentNameOf(uuid));
        }

        return uuidMap.get(uuid);
    }

    @Nullable
    public UUID getUUIDOf(@Nonnull String name) throws IOException, MojangAPIException, PlayerNotFoundException {
        BiMap<String, UUID> nameMap = uuidMap.inverse();
        if (nameMap.containsKey(name)) {
            return nameMap.get(name);
        }

        add(getCurrentUUIDOf(name), name);
        return uuidMap.inverse().get(name);
    }
}
