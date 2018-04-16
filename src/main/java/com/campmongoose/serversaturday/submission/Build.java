package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.menu.chest.BuildMenu;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Build {

    private List<String> description = Collections.singletonList("A Server Saturday Build");
    private boolean featured = false;
    private Location location;
    private String name;
    private List<String> resourcePack = Collections.singletonList("Vanilla");
    private boolean submitted = false;

    public Build(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public Build(boolean featured, boolean submitted, List<String> description, List<String> resourcePack, Location location, String name) {
        this.featured = featured;
        this.submitted = submitted;
        this.description = description;
        this.location = location;
        this.name = name;
        this.resourcePack = resourcePack;
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

    public List<String> getResourcePack() {
        return resourcePack;
    }

    public void setResourcePack(List<String> resourcePack) {
        this.resourcePack = resourcePack;
    }

    public void openMenu(Submitter submitter, Player player) {
        new BuildMenu(this, submitter, Bukkit.createInventory(null, 9, name), player.getUniqueId()).open(player);
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
