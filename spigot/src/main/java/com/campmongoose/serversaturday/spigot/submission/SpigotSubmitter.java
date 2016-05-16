package com.campmongoose.serversaturday.spigot.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.AbstractSubmitter;
import com.campmongoose.serversaturday.common.uuid.UUIDUtils;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.menu.chest.SubmitterMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SpigotSubmitter extends AbstractSubmitter<SpigotBuild, ItemStack, Location>
{
    private SpigotSubmitter(OfflinePlayer player)
    {
        super(player.getName(), player.getUniqueId());
    }

    private SpigotSubmitter(UUID uuid, ConfigurationSection cs)
    {
        super(uuid);
        try
        {
            this.name = UUIDUtils.getNameOf(uuid);
        }
        catch (IOException e)//NOSONAR
        {
            this.name = cs.getString(Config.NAME);
        }

        ConfigurationSection buildsCS = cs.getConfigurationSection(Config.BUILDS);
        buildsCS.getKeys(false).stream().filter(buildName -> !buildName.contains(".")).forEach(buildName -> builds.put(buildName, SpigotBuild.of(buildName, buildsCS.getConfigurationSection(buildName))));
    }

    @Override
    public SpigotBuild newBuild(String name, Location location)
    {
        builds.put(name, SpigotBuild.of(name, location));
        return getBuild(name);
    }

    @Override
    public ItemStack getMenuRepresentation()
    {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setDisplayName(name);
        skullMeta.setOwner(name);
        boolean hasNonFeaturedBuilds = false;
        for (SpigotBuild build : builds.values())
            if (build.submitted() && !build.featured())
                hasNonFeaturedBuilds = true;

        if (hasNonFeaturedBuilds)
        {
            skullMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            skullMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

    @Override
    public void openMenu(int page, UUID uuid)
    {
        new SubmitterMenu(this, page, Bukkit.createInventory(null, 54, MenuText.submitterMenu(name))).open(uuid);
    }

    @Override
    public void save(File file)
    {
        if (!file.exists())
        {
            try
            {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            }
            catch (IOException e)//NOSONAR
            {
                SpigotServerSaturday.getInstance().getLogger().severe(Messages.ioException(file));
                return;
            }
        }

        YamlConfiguration yaml = new YamlConfiguration();
        try
        {
            yaml.set(Config.NAME, UUIDUtils.getNameOf(uuid));
        }
        catch (IOException e)//NOSONAR
        {
            yaml.set(Config.NAME, name);
        }

        Map<String, Map<String, Object>> buildsMap = new HashMap<>();
        for (String buildName : builds.keySet())
            buildsMap.put(buildName, getBuild(buildName).serialize());

        yaml.set(Config.BUILDS, buildsMap);
        try
        {
            yaml.save(file);
        }
        catch (IOException e)//NOSONAR
        {
            SpigotServerSaturday.getInstance().getLogger().severe(Messages.ioException(file));
        }
    }

    @Override
    public void updateBuildName(SpigotBuild build, String newName)
    {
        builds.remove(build.getName());
        build.setName(newName);
        builds.put(build.getName(), build);
    }

    @Override
    public void updateBuildDescription(SpigotBuild build, List<String> description)
    {
        builds.remove(build.getName());
        build.setDescription(description);
        builds.put(build.getName(), build);
    }

    @Override
    public void updateBuildResourcePack(SpigotBuild build, String resourcePack)
    {
        builds.remove(build.getName());
        build.setResourcePack(resourcePack);
        builds.put(build.getName(), build);
    }

    @SuppressWarnings("WeakerAccess")
    public static SpigotSubmitter of(OfflinePlayer player)
    {
        return new SpigotSubmitter(player);
    }

    @SuppressWarnings("WeakerAccess")
    public static SpigotSubmitter of(UUID uuid, ConfigurationSection cs)
    {
        return new SpigotSubmitter(uuid, cs);
    }
}
