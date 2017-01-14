package com.campmongoose.serversaturday.sponge.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.AbstractSubmitter;
import com.campmongoose.serversaturday.common.uuid.UUIDUtils;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.SimpleConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.data.type.SkullTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SpongeSubmitter extends AbstractSubmitter<SpongeBuild, ItemStack, Location<World>>
{
    public SpongeSubmitter(@Nonnull Player player)
    {
        super(player.getName(), player.getUniqueId());
    }

    public SpongeSubmitter(@Nonnull UUID uuid, @Nonnull ConfigurationNode cn)
    {
        super(getName(uuid, cn), uuid);
        ConfigurationNode buildsCN = cn.getNode(Config.BUILDS);
        buildsCN.getChildrenMap().keySet().stream().filter(buildName -> !buildName.toString().contains(".")).forEach(buildName -> builds.put(buildName.toString(), SpongeBuild.of(buildName.toString(), buildsCN.getNode(buildName))));
    }

    private static String getName(UUID uuid, ConfigurationNode cn) {
        try
        {
            return UUIDUtils.getNameOf(uuid);
        }
        catch (IOException e)
        {
            return cn.getString(Config.NAME);
        }
    }

    @Nonnull
    @Override
    public SpongeBuild newBuild(@Nonnull String name, @Nonnull Location<World> location)
    {
        return builds.putIfAbsent(name, new SpongeBuild(name, location));
    }

    @Nonnull
    @Override
    public ItemStack getMenuRepresentation()
    {
        ItemStack itemStack = ItemStack.of(ItemTypes.SKULL, 1);
        itemStack.offer(Keys.SKULL_TYPE, SkullTypes.PLAYER);
        itemStack.offer(Keys.REPRESENTED_PLAYER, Sponge.getServer().getGameProfileManager().createProfile(uuid, name));
        boolean hasNonFeaturedBuilds = false;
        for (SpongeBuild build : builds.values())
            if (build.submitted() && !build.featured())
                hasNonFeaturedBuilds = true;

        if (hasNonFeaturedBuilds)
        {
            itemStack.offer(Keys.ITEM_ENCHANTMENTS, Collections.singletonList(new ItemEnchantment(Enchantments.AQUA_AFFINITY, 1)));
            itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
        }

        return itemStack;
    }

    @Override
    public void save(@Nonnull File file)
    {
        Logger logger = SpongeServerSaturday.getLogger();
        try
        {
            if (file.createNewFile())
                logger.info(Messages.newFile(file));
        }
        catch (IOException e)
        {
            SpongeServerSaturday.getLogger().error(Messages.ioException(file));
            return;
        }

        ConfigurationNode cn = SimpleConfigurationNode.root();
        try
        {
            cn.getNode(Config.NAME).setValue(UUIDUtils.getNameOf(uuid));
        }
        catch (IOException e)
        {
            cn.getNode(Config.NAME).setValue(name);
        }

        Map<String, Map<String, Object>> buildsMap = new HashMap<>();
        builds.forEach((key, value) -> buildsMap.put(key, value.serialize()));
        cn.getNode(Config.BUILDS).setValue(buildsMap);
        try
        {
            HoconConfigurationLoader.builder().setFile(file).build().save(cn);
        }
        catch (IOException e)
        {
            SpongeServerSaturday.getLogger().error(Messages.ioException(file));
        }
    }

    @Override
    public void updateBuildName(@Nonnull SpongeBuild build, @Nonnull String newName)
    {
        builds.remove(build.getName());
        build.setName(newName);
        builds.put(build.getName(), build);
    }

    @Override
    public void updateBuildDescription(@Nonnull SpongeBuild build, @Nonnull List<String> description)
    {
        builds.remove(build.getName());
        build.setDescription(description);
        builds.put(build.getName(), build);
    }

    @Override
    public void updateBuildResourcePack(@Nonnull SpongeBuild build, @Nonnull String resourcePack)
    {
        builds.remove(build.getName());
        build.setResourcePack(resourcePack);
        builds.put(build.getName(), build);
    }
}
