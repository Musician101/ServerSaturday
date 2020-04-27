package com.campmongoose.serversaturday.forge.submission;

import com.campmongoose.serversaturday.common.JsonUtils;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

public class ForgeSubmitter extends Submitter<ForgeBuild, ItemStack, Location> {

    public ForgeSubmitter(ServerPlayerEntity player) {
        super(player.getName().getString(), player.getUniqueID());
    }

    public ForgeSubmitter(String name, UUID uuid, Map<String, ForgeBuild> builds) {
        super(name, uuid);
        this.builds.putAll(builds);
    }

    @Nonnull
    @Override
    public ItemStack getMenuRepresentation() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ForgeBuild newBuild(@Nonnull String name, @Nonnull Location location) {
        builds.putIfAbsent(name, new ForgeBuild(name, location));
        return builds.get(name);
    }

    public void updateBuild(@Nonnull ForgeBuild newBuild) {
        builds.values().stream().filter(b -> b.getId() == newBuild.getId()).findFirst().ifPresent(build -> {
            builds.remove(build.getName());
            builds.put(newBuild.getName(), newBuild);
        });
    }

    public static class Serializer implements JsonDeserializer<ForgeSubmitter>, JsonSerializer<ForgeSubmitter> {

        @Override
        public ForgeSubmitter deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            UUID uuid = UUID.fromString(jsonObject.get(Config.UUID).getAsString());
            String name = jsonObject.get(Config.NAME).getAsString();
            Map<String, ForgeBuild> builds = StreamSupport.stream(jsonObject.getAsJsonArray(Config.BUILDS).spliterator(), false).map(build -> jsonDeserializationContext.<ForgeBuild>deserialize(build, ForgeBuild.class)).collect(Collectors.toMap(Build::getName, build -> build));
            return new ForgeSubmitter(name, uuid, builds);
        }

        @Override
        public JsonElement serialize(ForgeSubmitter submitter, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(Config.NAME, submitter.name);
            jsonObject.addProperty(Config.UUID, submitter.uuid.toString());
            jsonObject.add(Config.BUILDS, submitter.builds.values().stream().map(jsonSerializationContext::serialize).collect(JsonUtils.jsonArrayElementCollector()));
            return jsonObject;
        }
    }
}
