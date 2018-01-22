package com.campmongoose.serversaturday.spigot.submission;

import com.campmongoose.serversaturday.common.JsonUtils;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.submission.Build;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpigotBuild extends Build<SpigotBuild, ItemStack, Location, SpigotSubmitter, String> {

    public SpigotBuild(@Nonnull String name, @Nonnull Location location, @Nonnull List<String> description, @Nonnull List<String> resourcePacks, boolean featured, boolean submitted) {
        this(name, location);
        this.description = description;
        this.resourcePacks = resourcePacks;
        this.featured = featured;
        this.submitted = submitted;
    }

    public SpigotBuild(@Nonnull String name, @Nonnull Location location) {
        super(name, location);
    }

    @Nonnull
    @Override
    public ItemStack getMenuRepresentation(@Nonnull SpigotSubmitter submitter) {
        ItemStack itemStack = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(Collections.singletonList(submitter.getName()));
        itemMeta.setDisplayName(name);
        if (featured) {
            itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static class SpigotSerializer implements Serializer<SpigotBuild, ItemStack, Location, SpigotSubmitter, String> {

        @Override
        public SpigotBuild deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String name = jsonObject.get(Config.NAME).getAsString();
            Location location = jsonDeserializationContext.deserialize(jsonObject.getAsJsonObject(Config.LOCATION), Location.class);
            List<String> description = StreamSupport.stream(jsonObject.getAsJsonArray(Config.DESCRIPTION).spliterator(), false).map(JsonElement::getAsString).collect(Collectors.toList());
            List<String> resourcePacks = StreamSupport.stream(jsonObject.getAsJsonArray(Config.RESOURCE_PACKS).spliterator(), false).map(JsonElement::getAsString).collect(Collectors.toList());
            boolean featured = jsonObject.get(Config.FEATURED).getAsBoolean();
            boolean submitted = jsonObject.get(Config.SUBMITTED).getAsBoolean();
            return new SpigotBuild(name, location, description, resourcePacks, featured, submitted);
        }

        @Override
        public JsonElement serialize(SpigotBuild spigotBuild, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(Config.FEATURED, spigotBuild.featured);
            jsonObject.addProperty(Config.SUBMITTED, spigotBuild.submitted);
            jsonObject.addProperty(Config.NAME, spigotBuild.name);
            jsonObject.add(Config.LOCATION, jsonSerializationContext.serialize(spigotBuild.location));
            jsonObject.add(Config.DESCRIPTION, spigotBuild.description.stream().collect(JsonUtils.jsonArrayStringCollector()));
            jsonObject.add(Config.RESOURCE_PACKS, spigotBuild.resourcePacks.stream().collect(JsonUtils.jsonArrayStringCollector()));
            return jsonObject;
        }
    }
}
