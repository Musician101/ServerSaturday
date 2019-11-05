package com.campmongoose.serversaturday.sponge.submission;

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
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.SkullTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.GameProfileManager;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SpongeSubmitter extends Submitter<SpongeBuild, ItemStack, Location<World>, SpongeSubmitter, Text> {

    public SpongeSubmitter(@Nonnull Player player) {
        super(player.getName(), player.getUniqueId());
    }

    public SpongeSubmitter(String name, UUID uuid, Map<String, SpongeBuild> builds) {
        super(name, uuid);
        this.builds.putAll(builds);
    }

    @Nonnull
    @Override
    public ItemStack getMenuRepresentation() {
        ItemStack itemStack = ItemStack.of(ItemTypes.SKULL, 1);
        itemStack.offer(Keys.SKULL_TYPE, SkullTypes.PLAYER);
        GameProfileManager gpm = Sponge.getServer().getGameProfileManager();
        GameProfile gp = gpm.getCache().getById(uuid).orElse(gpm.createProfile(uuid, name));
        itemStack.offer(Keys.REPRESENTED_PLAYER, gp);
        itemStack.offer(Keys.DISPLAY_NAME, Text.of(gp.getName().orElse(name)));
        boolean hasNonFeaturedBuilds = false;
        for (SpongeBuild build : builds.values()) {
            if (build.submitted() && !build.featured()) {
                hasNonFeaturedBuilds = true;
            }
        }

        if (hasNonFeaturedBuilds) {
            itemStack.offer(Keys.ITEM_ENCHANTMENTS, Collections.singletonList(Enchantment.of(EnchantmentTypes.AQUA_AFFINITY, 1)));
            itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
        }

        return itemStack;
    }

    @Nonnull
    @Override
    public SpongeBuild newBuild(@Nonnull String name, @Nonnull Location<World> location) {
        builds.putIfAbsent(name, new SpongeBuild(name, location));
        return builds.get(name);
    }

    public static class SpongeSerializer implements Serializer<SpongeBuild, ItemStack, Location<World>, SpongeSubmitter, Text> {

        @Override
        public SpongeSubmitter deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            UUID uuid = UUID.fromString(jsonObject.get(Config.UUID).getAsString());
            String name = jsonObject.get(Config.NAME).getAsString();
            Map<String, SpongeBuild> builds = StreamSupport.stream(jsonObject.getAsJsonArray(Config.BUILDS).spliterator(), false).map(build -> jsonDeserializationContext.<SpongeBuild>deserialize(build, SpongeBuild.class)).collect(Collectors.toMap(Build::getName, build -> build));
            return new SpongeSubmitter(name, uuid, builds);
        }

        @Override
        public JsonElement serialize(SpongeSubmitter submitter, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(Config.NAME, submitter.name);
            jsonObject.addProperty(Config.UUID, submitter.uuid.toString());
            jsonObject.add(Config.BUILDS, submitter.builds.values().stream().map(jsonSerializationContext::serialize).collect(JsonUtils.jsonArrayElementCollector()));
            return jsonObject;
        }
    }
}
