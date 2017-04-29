package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.menu.chest.BuildMenu;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Build implements ConfigurationSerializable {

    private List<String> description = Collections.singletonList("A Server Saturday Build");
    private boolean featured = false;
    private Location location;
    private String name;
    private String resourcePack = "Vanilla";
    private boolean submitted = false;

    private Build(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    private Build(String name, ConfigurationSection cs) {
        this.name = name;
        this.featured = cs.getBoolean("featured");
        this.submitted = cs.getBoolean("submitted");
        this.location = Location.deserialize(cs.getConfigurationSection("location").getValues(true));
        this.resourcePack = cs.getString("resource_pack", "Vanilla");
        this.description = cs.getStringList("description");
    }

    public static Build of(String name, Location location) {
        return new Build(name, location);
    }

    public static Build of(String name, ConfigurationSection cs) {
        return new Build(name, cs);
    }

    public boolean featured() {
        return featured;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ItemStack getMenuRepresentation(Submitter submitter) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.replace(".", "");
    }

    public String getResourcePack() {
        return resourcePack;
    }

    public void setResourcePack(String resourcePack) {
        this.resourcePack = resourcePack;
    }

    public void openMenu(Submitter submitter, Player player) {
        new BuildMenu(this, submitter, Bukkit.createInventory(null, 9, name), player.getUniqueId()).open(player);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("featured", featured);
        map.put("submitted", submitted);
        map.put("description", description);
        map.put("location", location.serialize());
        map.put("name", name);
        map.put("resource_pack", resourcePack);
        return map;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public boolean submitted() {
        return submitted;
    }
}