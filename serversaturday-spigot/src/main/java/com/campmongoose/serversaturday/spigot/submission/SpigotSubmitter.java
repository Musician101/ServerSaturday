package com.campmongoose.serversaturday.spigot.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.spigot.SpigotRewardGiver;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.uuid.UUIDUtils;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SpigotSubmitter extends Submitter<SpigotBuild, ItemStack, Location> {

    public SpigotSubmitter(Player player) {
        super(player.getName(), player.getUniqueId());
    }

    //TODO going to change this over to JSON
    @Deprecated
    public SpigotSubmitter(@Nonnull UUID uuid, @Nonnull ConfigurationSection cs) {
        super(getName(uuid, cs.getString(Config.NAME)), uuid);
        //Backwards compat for old save formats
        if (cs.get(Config.BUILDS) instanceof ConfigurationSection) {
            ConfigurationSection buildsCS = cs.getConfigurationSection(Config.BUILDS);
            buildsCS.getKeys(false).stream().filter(buildName ->
                    !buildName.contains(".")).forEach(buildName -> builds.put(buildName, new SpigotBuild(buildsCS.getConfigurationSection(buildName))));
        }
        else {
            cs.getMapList(Config.BUILDS).forEach(object -> {
                MemoryConfiguration mc = new MemoryConfiguration();
                object.forEach((k, v) -> mc.set(k.toString(), v));
                builds.put(name, new SpigotBuild(mc));
            });
        }
    }

    //TODO update name when player joins the server
    @Deprecated
    private static String getName(UUID uuid, String name) {
        try {
            String retrievedName = UUIDUtils.getNameOf(uuid);
            return retrievedName == null ? name : retrievedName;
        }
        catch (IOException e) {
            return name;
        }
    }

    @Nonnull
    @Override
    public ItemStack getMenuRepresentation() {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setDisplayName(name);
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
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
        ServerSaturday<SpigotBuild, ItemStack, PlayerJoinEvent, Logger, Location, Player, SpigotRewardGiver, SpigotSubmissions, SpigotSubmitter> plugin = SpigotServerSaturday.instance();
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
            yaml.set(Config.NAME, UUIDUtils.getNameOf(uuid));
        }
        catch (IOException e) {
            yaml.set(Config.NAME, name);
        }

        yaml.set(Config.BUILDS, builds.values().stream().map(SpigotBuild::serialize).collect(Collectors.toList()));
        try {
            yaml.save(file);
        }
        catch (IOException e) {
            logger.severe(Messages.ioException(file));
        }
    }
}
