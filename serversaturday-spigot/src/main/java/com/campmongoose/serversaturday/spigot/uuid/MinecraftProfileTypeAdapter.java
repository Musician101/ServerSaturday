package com.campmongoose.serversaturday.spigot.uuid;

import com.campmongoose.serversaturday.spigot.uuid.UUIDUtils.MinecraftProfile;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.UUID;

public class MinecraftProfileTypeAdapter implements JsonDeserializer<MinecraftProfile>, JsonSerializer<MinecraftProfile> {

    @Override
    public MinecraftProfile deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jo = element.getAsJsonObject();
        String id = jo.get("id").getAsString();
        UUID uuid = UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
        String name = jo.get("name").getAsString();
        return new MinecraftProfile(uuid, name);
    }

    @Override
    public JsonElement serialize(MinecraftProfile profile, Type type, JsonSerializationContext context) {
        return null;
    }
}
