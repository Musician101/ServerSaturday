package com.campmongoose.serversaturday.json;

import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import static com.campmongoose.serversaturday.json.JsonUtils.GSON;

public class SubmitterTypeAdapter implements JsonDeserializer<Submitter>, JsonSerializer<Submitter> {

    @Override
    public Submitter deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        String name = offlinePlayer == null || offlinePlayer.getName() == null ? jsonObject.get("name").getAsString() : offlinePlayer.getName();
        Map<String, Build> builds = new HashMap<>();
        jsonObject.getAsJsonArray("builds").forEach(build -> {
            Build b = GSON.fromJson(build, Build.class);
            builds.put(b.getName(), b);
        });
        return new Submitter(builds, name, uuid);
    }

    @Override
    public JsonElement serialize(Submitter submitter, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        JsonArray builds = new JsonArray();
        submitter.getBuilds().forEach(build -> builds.add(GSON.toJsonTree(build)));
        jsonObject.add("builds", builds);
        UUID uuid = submitter.getUUID();
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        jsonObject.addProperty("name", player == null || player.getName() == null ? submitter.getName() : player.getName());
        jsonObject.addProperty("uuid", uuid.toString());
        return jsonObject;
    }
}
