package com.campmongoose.serversaturday.sponge.submission;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Optional;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class LocationSerializer implements JsonDeserializer<Location<World>>, JsonSerializer<Location<World>> {

    @Override
    public Location<World> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Optional<World> world = Sponge.getServer().getWorld(jsonObject.get("world").getAsString());
        if (!world.isPresent()) {
            throw new JsonParseException("World does not exist.");
        }

        double x = jsonObject.get("x").getAsDouble();
        double y = jsonObject.get("y").getAsDouble();
        double z = jsonObject.get("z").getAsDouble();
        return new Location<>(world.get(), x, y, z);
    }

    @Override
    public JsonElement serialize(Location<World> location, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("world", location.getExtent().getName());
        jsonObject.addProperty("x", location.getX());
        jsonObject.addProperty("y", location.getY());
        jsonObject.addProperty("z", location.getZ());
        return jsonObject;
    }
}
