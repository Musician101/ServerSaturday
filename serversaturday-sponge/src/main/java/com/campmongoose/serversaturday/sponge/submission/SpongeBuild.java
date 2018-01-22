package com.campmongoose.serversaturday.sponge.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.google.common.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.Queries;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.persistence.DataTranslator;
import org.spongepowered.api.data.persistence.DataTranslators;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SpongeBuild extends Build<ItemStack, Location<World>, SpongeSubmitter> {

    public SpongeBuild(String name, ConfigurationNode cn) {
        super(name);
        this.featured = cn.getNode(Config.FEATURED).getBoolean();
        this.submitted = cn.getNode(Config.SUBMITTED).getBoolean();
        this.location = deserializeLocation(cn.getNode(Config.LOCATION));
        this.resourcePack = cn.getNode(Config.RESOURCE_PACK).getList(Object::toString, Collections.singletonList(Config.VANILLA));
        try {
            this.description = cn.getNode(Config.DESCRIPTION).getList(TypeToken.of(String.class));
        }
        catch (ObjectMappingException e) {
            this.description = new ArrayList<>();
            SpongeServerSaturday.instance().map(ServerSaturday::getLogger).ifPresent(logger -> logger.error("An error occurred while parsing the description for " + name));
        }
    }

    public SpongeBuild(String name, Location<World> location) {
        super(name, location);
    }

    private Location<World> deserializeLocation(ConfigurationNode cn) {
        DataTranslator<ConfigurationNode> dt = DataTranslators.CONFIGURATION_NODE;
        DataView dv = dt.translate(cn);
        Server server = Sponge.getServer();
        World defaultWorld = new ArrayList<>(server.getWorlds()).get(0);
        World world = dv.getString(Queries.WORLD_NAME).map(name -> server.getWorld(name).orElse(defaultWorld)).orElse(defaultWorld);
        double x = dv.getDouble(Queries.POSITION_X).orElse(0D);
        double y = dv.getDouble(Queries.POSITION_Y).orElse(0D);
        double z = dv.getDouble(Queries.POSITION_Z).orElse(0D);
        return new Location<>(world, x, y, z);
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

    @Nonnull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put(Config.FEATURED, featured);
        map.put(Config.SUBMITTED, submitted);
        map.put(Config.DESCRIPTION, description);
        map.put(Config.LOCATION, DataTranslators.CONFIGURATION_NODE.translate(location.toContainer().remove(Queries.WORLD_ID).remove(Queries.BLOCK_TYPE)));
        map.put(Config.NAME, name);
        map.put(Config.RESOURCE_PACK, resourcePack);
        return map;
    }
}
