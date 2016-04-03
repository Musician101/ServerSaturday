package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.menu.chest.BuildMenu;
import com.campmongoose.serversaturday.menu.chest.ChestMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Build implements ConfigurationSerializable
{
    private boolean featured = false;
    private boolean submitted = false;
    private List<String> description = Collections.singletonList("A Server Saturday Build");
    private Location location;
    private String name;
    private String resourcePack = "Vanilla";

    private Build(String name, Location location)
    {
        this.name = name;
        this.location = location;
    }

    private Build(String name, ConfigurationSection cs)
    {
        this.name = name;
        this.featured = cs.getBoolean("featured");
        this.submitted = cs.getBoolean("submitted");
        this.location = Location.deserialize(cs.getConfigurationSection("location").getValues(true));
        this.resourcePack = cs.getString("resource_pack", "Vanilla");
        this.description = cs.getStringList("description");
    }

    public boolean featured()
    {
        return featured;
    }

    public void setFeatured(boolean featured)
    {
        this.featured = featured;
    }

    public boolean submitted()
    {
        return submitted;
    }

    public void setSubmitted(boolean submitted)
    {
        this.submitted = submitted;
    }

    public ItemStack getMenuRepresentation()
    {
        ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta skullMeta = (BookMeta) itemStack.getItemMeta();
        skullMeta.setTitle(name);
        if (featured)
        {
            skullMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            skullMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

    public List<String> getDescription()
    {
        return description;
    }

    public void setDescription(List<String> description)
    {
        this.description = description;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    @Override
    public Map<String, Object> serialize()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("featured", featured);
        map.put("submitted", submitted);
        map.put("description", description);
        map.put("location", location.serialize());
        map.put("name", name);
        map.put("resource_pack", resourcePack);
        return map;
    }

    public ChestMenu getMenu(ServerSaturday plugin, Submitter submitter, UUID viewer)
    {
        return new BuildMenu(plugin, this, submitter, Bukkit.createInventory(null, 9, name), viewer);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getResourcePack()
    {
        return resourcePack;
    }

    public void setResourcePack(String resourcePack)
    {
        this.resourcePack = resourcePack;
    }

    public static Build of(String name, Location location)
    {
        return new Build(name, location);
    }

    public static Build of(String name, ConfigurationSection cs)
    {
        return new Build(name, cs);
    }
}
