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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;

public class Location {

    @Nonnull
    private DimensionType dimension;
    private float pitch;
    private double x;
    private double y;
    private float yaw;
    private double z;

    public Location(@Nonnull PlayerEntity player) {
        this(player.dimension, player.func_226277_ct_(), player.func_226278_cu_(), player.func_226281_cx_(), player.rotationPitch, player.rotationYaw);
    }

    public Location(@Nonnull DimensionType dimension, double x, double y, double z, float pitch, float yaw) {
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    @Nonnull
    public DimensionType getDimension() {
        return dimension;
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
        dimension = player.dimension;
        Vec3d position = player.getPositionVector();
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
        Vec2f pitchYaw = player.getPitchYaw();
        this.pitch = pitchYaw.x;
        this.yaw = pitchYaw.y;
    }

    public static class Serializer implements JsonDeserializer<Location>, JsonSerializer<Location> {

        @Override
        public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            DimensionType dimension = DimensionType.byName(new ResourceLocation(jsonObject.get("world").getAsString()));
            if (dimension == null) {
                throw new JsonParseException("World does not exist.");
            }

            double x = jsonObject.get("x").getAsDouble();
            double y = jsonObject.get("y").getAsDouble();
            double z = jsonObject.get("z").getAsDouble();
            float pitch = jsonObject.get("pitch").getAsFloat();
            float yaw = jsonObject.get("yaw").getAsFloat();
            return new Location(dimension, x, y, z, yaw, pitch);
        }

        @Override
        public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("world", location.getDimension().getRegistryName().toString());
            jsonObject.addProperty("x", location.getX());
            jsonObject.addProperty("y", location.getY());
            jsonObject.addProperty("z", location.getZ());
            jsonObject.addProperty("pitch", location.getPitch());
            jsonObject.addProperty("yaw", location.getYaw());
            return jsonObject;
        }
    }
}
