package com.campmongoose.serversaturday.sponge.submission;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.submission.AbstractBuild;
import com.campmongoose.serversaturday.sponge.menu.chest.BuildMenu;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.LocatableSnapshot;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.data.translator.ConfigurateTranslator;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.custom.CustomInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.translation.FixedTranslation;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SpongeBuild extends AbstractBuild<ItemStack, Location<World>, String, SpongeSubmitter>
{
    private SpongeBuild(String name, ConfigurationNode cn)
    {
        super(name);
        this.featured = cn.getNode(Config.FEATURED).getBoolean();
        this.submitted = cn.getNode(Config.SUBMITTED).getBoolean();
        this.location = deserializeLocation(cn.getNode(Config.LOCATION));
        this.resourcePack = cn.getNode(Config.RESOURCE_PACK).getString();
        try
        {
            this.description = cn.getNode(Config.DESCRIPTION).getList(TypeToken.of(String.class));
        }
        catch (ObjectMappingException e)//NOSONAR
        {
            this.description = new ArrayList<>();
            LoggerFactory.getLogger(Reference.ID).error("An error occurred while parsing the description for " + name);
        }
    }

    private SpongeBuild(String name, Location<World> location)
    {
        super(name, location);
    }

    private Location<World> deserializeLocation(ConfigurationNode cn)
    {
        ConfigurateTranslator ct = ConfigurateTranslator.instance();
        DataView dv = ct.translateFrom(cn);
        @SuppressWarnings({"unchecked", "OptionalGetWithoutIsPresent"})
        Optional<Location<World>> olw = Sponge.getDataManager().getBuilder(LocatableSnapshot.class).get().build(dv).get().getLocation();
        //noinspection OptionalGetWithoutIsPresent
        return olw.get();
    }

    @Override
    public ItemStack getMenuRepresentation(SpongeSubmitter submitter)
    {
        ItemStack itemStack = ItemStack.of(ItemTypes.BOOK, 1);
        itemStack.offer(Keys.ITEM_LORE, Collections.singletonList(Text.of(submitter.getName())));
        itemStack.offer(Keys.DISPLAY_NAME, Text.of(name));
        if (featured)
        {
            itemStack.offer(Keys.ITEM_ENCHANTMENTS, Collections.singletonList(new ItemEnchantment(Enchantments.AQUA_AFFINITY, 1)));
            itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
        }

        return itemStack;
    }

    @Override
    public Map<String, Object> serialize()
    {
        Map<String, Object> map = new HashMap<>();
        map.put(Config.FEATURED, featured);
        map.put(Config.SUBMITTED, submitted);
        map.put(Config.DESCRIPTION, description);
        map.put(Config.LOCATION, ConfigurateTranslator.instance().translateData(location.toContainer()));
        map.put(Config.NAME, name);
        map.put(Config.RESOURCE_PACK, resourcePack);
        return map;
    }

    @Override
    public void openMenu(SpongeSubmitter submitter, UUID uuid)
    {
        new BuildMenu(this, submitter, CustomInventory.builder().size(9).name(new FixedTranslation(name)).build(), uuid).open(uuid);
    }

    public static SpongeBuild of(String name, Location<World> location)
    {
        return new SpongeBuild(name, location);
    }

    public static SpongeBuild of(String name, ConfigurationNode cn)
    {
        return new SpongeBuild(name, cn);
    }
}
