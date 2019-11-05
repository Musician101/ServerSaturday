package com.campmongoose.serversaturday.sponge.submission;

import com.campmongoose.serversaturday.common.JsonUtils;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.submission.Build;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SpongeBuild extends Build<SpongeBuild, ItemStack, Location<World>, SpongeSubmitter, Text> {

    public SpongeBuild(@Nonnull String name, @Nonnull Location<World> location, @Nonnull List<Text> description, @Nonnull List<Text> resourcePacks, boolean featured, boolean submitted) {
        this(name, location);
        this.description = description;
        this.resourcePacks = resourcePacks;
        this.featured = featured;
        this.submitted = submitted;
    }

    public SpongeBuild(String name, Location<World> location) {
        super(name, location);
    }

    @Nonnull
    @Override
    public ItemStack getMenuRepresentation(@Nonnull SpongeSubmitter submitter) {
        ItemStack itemStack = ItemStack.of(ItemTypes.BOOK, 1);
        itemStack.offer(Keys.ITEM_LORE, Collections.singletonList(Text.of(submitter.getName())));
        itemStack.offer(Keys.DISPLAY_NAME, Text.of(name));
        if (featured) {
            itemStack.offer(Keys.ITEM_ENCHANTMENTS, Collections.singletonList(Enchantment.of(EnchantmentTypes.AQUA_AFFINITY, 1)));
            itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
        }

        return itemStack;
    }

    public static class SpongeSerializer implements Serializer<SpongeBuild, ItemStack, Location<World>, SpongeSubmitter, Text> {

        @Override
        public SpongeBuild deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String name = jsonObject.get(Config.NAME).getAsString();
            Location<World> location = jsonDeserializationContext.deserialize(jsonObject.getAsJsonObject(Config.LOCATION), new TypeToken<Location<World>>(){}.getType());
            List<Text> description = StreamSupport.stream(jsonObject.getAsJsonArray(Config.DESCRIPTION).spliterator(), false).map(json -> TextSerializers.JSON.deserialize(json.toString())).collect(Collectors.toList());
            List<Text> resourcePacks = StreamSupport.stream(jsonObject.getAsJsonArray(Config.RESOURCE_PACK).spliterator(), false).map(json -> TextSerializers.JSON.deserialize(json.toString())).collect(Collectors.toList());
            boolean featured = jsonObject.get(Config.FEATURED).getAsBoolean();
            boolean submitted = jsonObject.get(Config.SUBMITTED).getAsBoolean();
            return new SpongeBuild(name, location, description, resourcePacks, featured, submitted);
        }

        @Override
        public JsonElement serialize(SpongeBuild SpongeBuild, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(Config.FEATURED, SpongeBuild.featured);
            jsonObject.addProperty(Config.SUBMITTED, SpongeBuild.submitted);
            jsonObject.addProperty(Config.NAME, SpongeBuild.name);
            jsonObject.add(Config.LOCATION, jsonSerializationContext.serialize(SpongeBuild.location));
            jsonObject.add(Config.DESCRIPTION, SpongeBuild.description.stream().map(text -> new Gson().fromJson(TextSerializers.JSON.serialize(text), JsonObject.class)).collect(JsonUtils.jsonArrayElementCollector()));
            jsonObject.add(Config.RESOURCE_PACK, SpongeBuild.resourcePacks.stream().map(text -> new Gson().fromJson(TextSerializers.JSON.serialize(text), JsonObject.class)).collect(JsonUtils.jsonArrayElementCollector()));
            return jsonObject;
        }
    }
}
