package com.campmongoose.serversaturday.spigot.submission;

import com.campmongoose.serversaturday.common.JsonUtils;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SpigotSubmitter extends Submitter<SpigotBuild, ItemStack, Location, SpigotSubmitter, String> {

    public SpigotSubmitter(Player player) {
        super(player.getName(), player.getUniqueId());
    }

    public SpigotSubmitter(String name, UUID uuid, Map<String, SpigotBuild> builds) {
        super(name, uuid);
        this.builds.putAll(builds);
    }

    @Nonnull
    @Override
    public ItemStack getMenuRepresentation() {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setDisplayName(name);
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        boolean hasNonFeaturedBuilds = false;
        for (SpigotBuild build : builds.values()) {
            if (build.submitted() && !build.featured()) {
                hasNonFeaturedBuilds = true;
            }
        }

        if (hasNonFeaturedBuilds) {
            skullMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            skullMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

    @Nonnull
    @Override
    public SpigotBuild newBuild(@Nonnull String name, @Nonnull Location location) {
        builds.putIfAbsent(name, new SpigotBuild(name, location));
        return builds.get(name);
    }

    public static class SpigotSerializer implements Serializer<SpigotBuild, ItemStack, Location, SpigotSubmitter, String> {

        @Override
        public SpigotSubmitter deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            UUID uuid = UUID.fromString(jsonObject.get(Config.UUID).getAsString());
            String name = jsonObject.get(Config.NAME).getAsString();
            Map<String, SpigotBuild> builds = StreamSupport.stream(jsonObject.getAsJsonArray(Config.BUILDS).spliterator(), false).map(build -> jsonDeserializationContext.<SpigotBuild>deserialize(build, SpigotBuild.class)).collect(Collectors.toMap(Build::getName, build -> build));
            return new SpigotSubmitter(name, uuid, builds);
        }

        @Override
        public JsonElement serialize(SpigotSubmitter submitter, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(Config.NAME, submitter.name);
            jsonObject.addProperty(Config.UUID, submitter.uuid.toString());
            jsonObject.add(Config.BUILDS, submitter.builds.values().stream().map(jsonSerializationContext::serialize).collect(JsonUtils.jsonArrayElementCollector()));
            return jsonObject;
        }
    }
}
