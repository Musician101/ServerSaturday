package com.campmongoose.serversaturday.spigot.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.AbstractSubmitter;
import com.campmongoose.serversaturday.common.uuid.UUIDUtils;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SpigotSubmitter extends AbstractSubmitter<SpigotBuild, ItemStack, Location> {

    public SpigotSubmitter(Player player) {
        super(player.getName(), player.getUniqueId());
    }

    public SpigotSubmitter(UUID uuid, ConfigurationSection cs) {
        super(getName(uuid, cs), uuid);
        ConfigurationSection buildsCS = cs.getConfigurationSection(Config.BUILDS);
        buildsCS.getKeys(false).stream().filter(buildName -> !buildName.contains(".")).forEach(buildName -> builds.put(buildName, new SpigotBuild(buildName, buildsCS.getConfigurationSection(buildName))));
    }

    private static String getName(UUID uuid, ConfigurationSection cs) {
        try {
            return UUIDUtils.getNameOf(uuid);
        }
        catch (IOException e) {
            return cs.getString(Config.NAME);
        }
    }

    @Nonnull
    @Override
    public ItemStack getMenuRepresentation() {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setDisplayName(name);
        skullMeta.setOwner(name);
        boolean hasNonFeaturedBuilds = false;
        for (SpigotBuild build : builds.values()) {
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

    @Nonnull
    @Override
    public SpigotBuild newBuild(@Nonnull String name, @Nonnull Location location) {
        return builds.putIfAbsent(name, new SpigotBuild(name, location));
    }

    @Override
    public void save(@Nonnull File file) {
        Logger logger = SpigotServerSaturday.instance().getLogger();
        try {
            if (file.createNewFile()) {
                logger.info(Messages.newFile(file));
            }
        }
        catch (IOException e) {
            logger.severe(Messages.ioException(file));
            return;
        }

        YamlConfiguration yaml = new YamlConfiguration();
        try {
            yaml.set(Config.NAME, UUIDUtils.getNameOf(uuid));
        }
        catch (IOException e) {
            yaml.set(Config.NAME, name);
        }

        Map<String, Map<String, Object>> buildsMap = new HashMap<>();
        builds.forEach((buildName, build) -> buildsMap.put(buildName, build.serialize()));
        yaml.set(Config.BUILDS, buildsMap);
        try {
            yaml.save(file);
        }
        catch (IOException e) {
            logger.severe(Messages.ioException(file));
        }
    }

    @Override
    public void updateBuildDescription(@Nonnull SpigotBuild build, @Nonnull List<String> description) {
        builds.remove(build.getName());
        build.setDescription(description);
        builds.put(build.getName(), build);
    }

    @Override
    public void updateBuildName(@Nonnull SpigotBuild build, @Nonnull String newName) {
        builds.remove(build.getName());
        build.setName(newName);
        builds.put(build.getName(), build);
    }

    @Override
    public void updateBuildResourcePack(@Nonnull SpigotBuild build, @Nonnull String resourcePack) {
        builds.remove(build.getName());
        build.setResourcePack(resourcePack);
        builds.put(build.getName(), build);
    }
}
