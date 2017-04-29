package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.menu.chest.SubmitterMenu;
import com.campmongoose.serversaturday.util.UUIDCacheException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Submitter {

    private final Map<String, Build> builds = new HashMap<>();
    private final UUID uuid;
    private String name;

    private Submitter(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    private Submitter(UUID uuid, ConfigurationSection cs) {
        this.uuid = uuid;
        try {
            this.name = ServerSaturday.instance().getUUIDCache().getNameOf(uuid);
        }
        catch (UUIDCacheException e) {
            name = cs.getString("name");
        }

        ConfigurationSection buildsCS = cs.getConfigurationSection("builds");
        buildsCS.getKeys(false).stream().filter(name -> !name.contains(".")).forEach(name -> builds.put(name, Build.of(name, buildsCS.getConfigurationSection(name))));
    }

    public static Submitter of(Player player) {
        return new Submitter(player);
    }

    public static Submitter of(UUID uuid, ConfigurationSection cs) {
        return new Submitter(uuid, cs);
    }

    public Build getBuild(String name) {
        return builds.get(name);
    }

    public List<Build> getBuilds() {
        List<Build> list = new ArrayList<>();
        list.addAll(builds.values());
        return list;
    }

    public ItemStack getMenuRepresentation() {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setDisplayName(name);
        skullMeta.setOwner(name);
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

    public UUID getUUID() {
        return uuid;
    }

    public Build newBuild(String name, Location location) {
        builds.put(name, Build.of(name, location));
        return getBuild(name);
    }

    public void openMenu(int page, Player player) {
        new SubmitterMenu(this, page, Bukkit.createInventory(null, 54, name + "'s Builds")).open(player);
    }

    public void removeBuild(String name) {
        builds.remove(name);
    }

    public void save(File file) {
        if (!file.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            }
            catch (IOException e) {
                ServerSaturday.instance().getLogger().severe("An error occurred while saving " + file.getName());
                return;
            }
        }

        YamlConfiguration yaml = new YamlConfiguration();
        String name;
        try {
            name = ServerSaturday.instance().getUUIDCache().getNameOf(uuid);
        }
        catch (UUIDCacheException e) {
            name = this.name;
        }
        yaml.set("name", name);

        Map<String, Map<String, Object>> buildsMap = new HashMap<>();
        for (String buildName : builds.keySet()) {
            buildsMap.put(buildName, getBuild(buildName).serialize());
        }

        yaml.set("builds", buildsMap);
        try {
            yaml.save(file);
        }
        catch (IOException e) {
            ServerSaturday.instance().getLogger().severe("An error occurred while saving " + file.getName());
        }
    }

    public void updateBuildDescription(Build build, List<String> description) {
        builds.remove(build.getName());
        build.setDescription(description);
        builds.put(build.getName(), build);
    }

    public void updateBuildName(Build build, String newName) {
        builds.remove(build.getName());
        build.setName(newName);
        builds.put(build.getName(), build);
    }

    public void updateBuildResourcePack(Build build, String resourcePack) {
        builds.remove(build.getName());
        build.setResourcePack(resourcePack);
        builds.put(build.getName(), build);
    }
}
