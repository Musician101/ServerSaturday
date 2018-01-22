package com.campmongoose.serversaturday.spigot.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.submission.Build;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpigotBuild extends Build<ItemStack, Location, SpigotSubmitter> {

    //TODO going to change this over to JSON
    @Deprecated
    public SpigotBuild(@Nonnull ConfigurationSection cs) {
        super(cs.getString(Config.NAME));
        this.featured = cs.getBoolean(Config.FEATURED);
        this.submitted = cs.getBoolean(Config.SUBMITTED);
        Object locationCS = cs.get(Config.LOCATION);
        if (locationCS instanceof ConfigurationSection) {
            this.location = Location.deserialize(cs.getConfigurationSection(Config.LOCATION).getValues(true));
        }
        else {
            Map<String, Object> location = new HashMap<>();
            ((Map) cs.get(Config.LOCATION)).forEach((k, v) -> location.put(k.toString(), v));
            this.location = Location.deserialize(location);
        }
        if (cs.contains(Config.RESOURCE_PACK)) {
            if (cs.get(Config.RESOURCE_PACK) instanceof String) {
                this.resourcePack = Collections.singletonList(cs.getString(Config.RESOURCE_PACK));
            }
            else if (cs.get(Config.RESOURCE_PACK) instanceof List) {
                this.resourcePack = cs.getStringList(Config.RESOURCE_PACK);
            }
        }

        this.description = cs.getStringList(Config.DESCRIPTION);
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

    @Nonnull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put(Config.FEATURED, featured);
        map.put(Config.SUBMITTED, submitted);
        map.put(Config.DESCRIPTION, description);
        map.put(Config.LOCATION, location.serialize());
        map.put(Config.NAME, name);
        map.put(Config.RESOURCE_PACK, resourcePack);
        return map;
    }
}
