package com.campmongoose.serversaturday.forge.submission;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class Location {

    @Nonnull
    private RegistryKey<World> worldKey;
    private float pitch;
    private double x;
    private double y;
    private float yaw;
    private double z;

    public Location(@Nonnull PlayerEntity player) {
        this(player.getEntityWorld().getDimensionKey(), player.getPosX(), player.getPosY(), player.getPosZ(), player.rotationPitch, player.rotationYaw);
    }

    public Location(@Nonnull RegistryKey<World> worldKey, double x, double y, double z, float pitch, float yaw) {
        this.worldKey = worldKey;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    @Nonnull
    public RegistryKey<World> getWorldKey() {
        return worldKey;
    }

    public float getPitch() {
        return pitch;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public float getYaw() {
        return yaw;
    }

    public double getZ() {
        return z;
    }

    public void update(@Nonnull ServerPlayerEntity player) {
        worldKey = player.getEntityWorld().getDimensionKey();
        Vector3d position = player.getPositionVec();
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
        Vector2f pitchYaw = player.getPitchYaw();
        this.pitch = pitchYaw.x;
        this.yaw = pitchYaw.y;
    }

    public static class Serializer implements JsonDeserializer<Location>, JsonSerializer<Location> {

        @Override
        public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            RegistryKey<World> worldKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, ResourceLocation.create(jsonObject.get("world").getAsString().split("/")[1], ':'));
            double x = jsonObject.get("x").getAsDouble();
            double y = jsonObject.get("y").getAsDouble();
            double z = jsonObject.get("z").getAsDouble();
            float pitch = jsonObject.get("pitch").getAsFloat();
            float yaw = jsonObject.get("yaw").getAsFloat();
            return new Location(worldKey, x, y, z, yaw, pitch);
        }

        @Override
        public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("world", location.getWorldKey().getLocation().toString());
            jsonObject.addProperty("x", location.getX());
            jsonObject.addProperty("y", location.getY());
            jsonObject.addProperty("z", location.getZ());
            jsonObject.addProperty("pitch", location.getPitch());
            jsonObject.addProperty("yaw", location.getYaw());
            return jsonObject;
        }
    }
}
