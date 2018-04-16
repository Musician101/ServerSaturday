package com.campmongoose.serversaturday.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.util.UUIDTypeAdapter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.event.Listener;

public class UUIDUtils implements Listener {

    private UUIDUtils() {

    }

    @Nullable
    private static JsonObject get(@Nonnull String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        return new Gson().fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class);
    }

    @Nonnull
    private static String getCurrentName(@Nonnull UUID uuid) throws IOException, MojangAPIException, PlayerNotFoundException {
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
    private static UUID getCurrentUUID(@Nonnull String name) throws IOException, MojangAPIException, PlayerNotFoundException {
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
    public static String getNameOf(@Nonnull UUID uuid) throws IOException, MojangAPIException, PlayerNotFoundException {
        return getCurrentName(uuid);
    }

    @Nonnull
    public static UUID getUUIDOf(@Nonnull String name) throws IOException, MojangAPIException, PlayerNotFoundException {
        return getCurrentUUID(name);
    }
}
