package com.campmongoose.serversaturday.spigot.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.AbstractSubmitter;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.uuid.UUIDCacheException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    public SpigotSubmitter(@Nonnull UUID uuid, @Nonnull ConfigurationSection cs) {
        super(getName(uuid, cs.getString(Config.NAME)), uuid);
        //Backwards compat for old save formats
        if (cs.get(Config.BUILDS) instanceof ConfigurationSection) {
            ConfigurationSection buildsCS = cs.getConfigurationSection(Config.BUILDS);
            buildsCS.getKeys(false).stream().filter(buildName ->
                    !buildName.contains(".")).forEach(buildName -> builds.put(buildName, new SpigotBuild(buildName, buildsCS.getConfigurationSection(buildName))));
        }
        else {
            cs.getList(Config.BUILDS).forEach(object -> {
                ConfigurationSection buildCS = (ConfigurationSection) object;
                String name = cs.getString(Config.NAME);
                builds.put(name, new SpigotBuild(name, buildCS));
            });
        }
    }

    private static String getName(UUID uuid, String name) {
        try {
            return SpigotServerSaturday.instance().getUUIDCache().getNameOf(uuid);
        }
        catch (UUIDCacheException e) {
            return name;
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
        builds.putIfAbsent(name, new SpigotBuild(name, location));
        return builds.get(name);
    }

    @Override
    public void save(@Nonnull File file) {
        SpigotServerSaturday plugin = SpigotServerSaturday.instance();
        Logger logger = plugin.getLogger();
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
            yaml.set(Config.NAME, plugin.getUUIDCache().getNameOf(uuid));
        }
        catch (UUIDCacheException e) {
            yaml.set(Config.NAME, name);
        }

        List<Map<String, Object>> builds = new ArrayList<>();
        this.builds.forEach((buildName, build) -> builds.add(build.serialize()));
        yaml.set(Config.BUILDS, builds);
        try {
            yaml.save(file);
        }
        catch (IOException e) {
            logger.severe(Messages.ioException(file));
        }
    }
}
