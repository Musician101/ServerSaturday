package com.campmongoose.serversaturday.json;

import com.campmongoose.serversaturday.submission.Build;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

import static com.campmongoose.serversaturday.json.JsonUtils.GSON;

public class BuildTypeAdapter implements JsonDeserializer<Build>, JsonSerializer<Build> {

    private JsonArray convertToJsonArray(List<String> list) {
        JsonArray jsonArray = new JsonArray();
        list.stream().map(JsonPrimitive::new).forEach(jsonArray::add);
        return jsonArray;
    }

    private List<String> convertToList(JsonArray jsonArray) {
        List<String> list = new ArrayList<>();
        jsonArray.forEach(element -> list.add(element.getAsString()));
        return list;
    }

    @Override
    public Build deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        boolean featured = jsonObject.get("featured").getAsBoolean();
        boolean submitted = jsonObject.get("submitted").getAsBoolean();
        List<String> description = convertToList(jsonObject.getAsJsonArray("description"));
        List<String> resourcePack = convertToList(jsonObject.getAsJsonArray("resource_pack"));
        Location location = GSON.fromJson(jsonObject.getAsJsonObject("location"), Location.class);
        String name = jsonObject.get("name").getAsString();
        return new Build(featured, submitted, description, resourcePack, location, name);
    }

    @Override
    public JsonElement serialize(Build build, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("featured", build.featured());
        jsonObject.addProperty("submitted", build.submitted());
        jsonObject.add("description", convertToJsonArray(build.getDescription()));
        jsonObject.add("resource_pack", convertToJsonArray(build.getResourcePack()));
        jsonObject.add("location", GSON.toJsonTree(build.getLocation()));
        jsonObject.addProperty("name", build.getName());
        return jsonObject;
    }
}
