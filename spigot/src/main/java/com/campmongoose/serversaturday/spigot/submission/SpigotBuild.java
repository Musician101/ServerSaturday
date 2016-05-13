package com.campmongoose.serversaturday.spigot.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.submission.AbstractBuild;
import com.campmongoose.serversaturday.spigot.menu.chest.BuildMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpigotBuild extends AbstractBuild<ItemStack, Location, String, SpigotSubmitter>
{
    private SpigotBuild(String name, ConfigurationSection cs)
    {
        super(name);
        this.featured = cs.getBoolean(Config.FEATURED);
        this.submitted = cs.getBoolean(Config.SUBMITTED);
        this.location = Location.deserialize(cs.getConfigurationSection(Config.LOCATION).getValues(true));
        this.resourcePack = cs.getString(Config.RESOURCE_PACK, Config.VANILLA);
        this.description = cs.getStringList(Config.DESCRIPTION);
    }

    private SpigotBuild(String name, Location location)
    {
        super(name, location);
    }

    @Override
    public ItemStack getMenuRepresentation(SpigotSubmitter submitter)
    {
        ItemStack itemStack = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(Collections.singletonList(submitter.getName()));
        itemMeta.setDisplayName(name);
        if (featured)
        {
            itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public Map<String, Object> serialize()
    {
        Map<String, Object> map = new HashMap<>();
        map.put(Config.FEATURED, featured);
        map.put(Config.SUBMITTED, submitted);
        map.put(Config.DESCRIPTION, description);
        map.put(Config.LOCATION, location.serialize());
        map.put(Config.NAME, name);
        map.put(Config.RESOURCE_PACK, resourcePack);
        return map;
    }

    @Override
    public void openMenu(SpigotSubmitter submitter, UUID uuid)
    {
        new BuildMenu(this, submitter, Bukkit.createInventory(null, 9, name), uuid).open(uuid);
    }

    public static SpigotBuild of(String name, Location location)
    {
        return new SpigotBuild(name, location);
    }

    public static SpigotBuild of(String name, ConfigurationSection cs)
    {
        return new SpigotBuild(name, cs);
    }
}
