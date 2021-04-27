package com.campmongoose.serversaturday.forge.submission;

import com.campmongoose.serversaturday.common.JsonUtils;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.submission.Build;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;

public class ForgeBuild extends Build<Location, String> {

    @Nonnull
    private final UUID uuid;

    public ForgeBuild(@Nonnull String name, @Nonnull Location location, @Nonnull List<String> description, @Nonnull List<String> resourcePacks, boolean featured, boolean submitted, @Nonnull UUID uuid) {
        super(name, location);
        this.description = description;
        this.resourcePacks = resourcePacks;
        this.featured = featured;
        this.submitted = submitted;
        this.uuid = uuid;
    }

    public ForgeBuild(@Nonnull String name, @Nonnull Location location, @Nonnull List<String> description, @Nonnull List<String> resourcePacks, boolean featured, boolean submitted) {
        this(name, location, description, resourcePacks, featured, submitted, UUID.randomUUID());
    }

    public ForgeBuild(@Nonnull String name, @Nonnull Location location) {
        super(name, location);
        this.uuid = UUID.randomUUID();
        description.add("A Server Saturday Build.");
    }

    @Nonnull
    @Override
    public ItemStack getMenuRepresentation(@Nonnull ForgeSubmitter submitter) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    public UUID getId() {
        return uuid;
    }

    public static class Serializer implements JsonDeserializer<ForgeBuild>, JsonSerializer<ForgeBuild> {

        @Override
        public ForgeBuild deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String name = jsonObject.get(Config.NAME).getAsString();
            Location location = jsonDeserializationContext.deserialize(jsonObject.getAsJsonObject(Config.LOCATION), Location.class);
            List<String> description = StreamSupport.stream(jsonObject.getAsJsonArray(Config.DESCRIPTION).spliterator(), false).map(JsonElement::getAsString).collect(Collectors.toList());
            List<String> resourcePacks = StreamSupport.stream(jsonObject.getAsJsonArray(Config.RESOURCE_PACK).spliterator(), false).map(JsonElement::getAsString).collect(Collectors.toList());
            boolean featured = jsonObject.get(Config.FEATURED).getAsBoolean();
            boolean submitted = jsonObject.get(Config.SUBMITTED).getAsBoolean();
            UUID uuid = UUID.fromString(jsonObject.get(Config.UUID).getAsString());
            return new ForgeBuild(name, location, description, resourcePacks, featured, submitted, uuid);
        }

        @Override
        public JsonElement serialize(ForgeBuild build, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(Config.FEATURED, build.featured);
            jsonObject.addProperty(Config.SUBMITTED, build.submitted);
            jsonObject.addProperty(Config.NAME, build.name);
            jsonObject.add(Config.LOCATION, jsonSerializationContext.serialize(build.location));
            jsonObject.add(Config.DESCRIPTION, build.description.stream().collect(JsonUtils.jsonArrayStringCollector()));
            jsonObject.add(Config.RESOURCE_PACK, build.resourcePacks.stream().collect(JsonUtils.jsonArrayStringCollector()));
            jsonObject.addProperty(Config.UUID, build.getId().toString());
            return jsonObject;
        }
    }
}
