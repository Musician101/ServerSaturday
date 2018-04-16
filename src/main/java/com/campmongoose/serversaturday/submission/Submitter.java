package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.menu.chest.SubmitterMenu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Submitter {

    private final Map<String, Build> builds;
    private final UUID uuid;
    private String name;

    public Submitter(Map<String, Build> builds, String name, UUID uuid) {
        this.builds = builds;
        this.name = name;
        this.uuid = uuid;
    }

    public Submitter(OfflinePlayer player) {
        this.builds = new HashMap<>();
        this.name = player.getName();
        this.uuid = player.getUniqueId();
    }

    public Build getBuild(String name) {
        return builds.get(name);
    }

    public List<Build> getBuilds() {
        return new ArrayList<>(builds.values());
    }

    public ItemStack getMenuRepresentation() {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setDisplayName(name);
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        boolean hasNonFeaturedBuilds = false;
        for (Build build : builds.values()) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Build newBuild(String name, Location location) {
        builds.put(name, new Build(name, location));
        return getBuild(name);
    }

    public void openMenu(int page, Player player) {
        new SubmitterMenu(this, page, Bukkit.createInventory(null, 54, name + "'s Builds")).open(player);
    }

    public void removeBuild(String name) {
        builds.remove(name);
    }

    public void updateBuildName(Build build, String newName) {
        builds.remove(build.getName());
        build.setName(newName);
        builds.put(build.getName(), build);
    }
}
