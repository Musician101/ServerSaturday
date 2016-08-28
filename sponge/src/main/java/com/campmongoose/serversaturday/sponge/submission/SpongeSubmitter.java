package com.campmongoose.serversaturday.sponge.submission;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.AbstractSubmitter;
import com.campmongoose.serversaturday.common.uuid.UUIDUtils;
import com.campmongoose.serversaturday.sponge.menu.chest.SubmitterMenu;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.SimpleConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.data.type.SkullTypes;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.custom.CustomInventory;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.text.translation.FixedTranslation;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SpongeSubmitter extends AbstractSubmitter<SpongeBuild, ItemStack, Location<World>>
{
    private SpongeSubmitter(GameProfile profile)
    {
        //noinspection OptionalGetWithoutIsPresent
        super(profile.getName().get(), profile.getUniqueId());//NOSONAR
    }

    private SpongeSubmitter(UUID uuid, ConfigurationNode cn)
    {
        super(uuid);
        try
        {
            this.name = UUIDUtils.getNameOf(uuid);
        }
        catch (IOException e)//NOSONAR
        {
            this.name = cn.getString(Config.NAME);
        }

        ConfigurationNode buildsCN = cn.getNode(Config.BUILDS);
        buildsCN.getChildrenMap().keySet().stream().filter(buildName -> !buildName.toString().contains(".")).forEach(buildName -> builds.put(buildName.toString(), SpongeBuild.of(buildName.toString(), buildsCN.getNode(buildName))));
    }

    @Override
    public SpongeBuild newBuild(String name, Location<World> location)
    {
        builds.put(name, SpongeBuild.of(name, location));
        return getBuild(name);
    }

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
    public void openMenu(int page, UUID uuid)
    {
        new SubmitterMenu(this, page, CustomInventory.builder().size(54).name(new FixedTranslation(MenuText.submitterMenu(name))).build()).open(uuid);
    }

    @Override
    public void save(File file)
    {
        if (!file.exists())
        {
            try
            {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();//NOSONAR
            }
            catch (IOException e)//NOSONAR
            {
                LoggerFactory.getLogger(Reference.ID).error(Messages.ioException(file));
                return;
            }
        }

        ConfigurationNode cn = SimpleConfigurationNode.root();
        try
        {
            cn.getNode(Config.NAME).setValue(UUIDUtils.getNameOf(uuid));
        }
        catch (IOException e)//NOSONAR
        {
            cn.getNode(Config.NAME).setValue(name);
        }

        Map<String, Map<String, Object>> buildsMap = new HashMap<>();
        for (String buildName : builds.keySet())
            buildsMap.put(buildName, getBuild(buildName).serialize());

        cn.getNode(Config.BUILDS).setValue(buildsMap);
        try
        {
            HoconConfigurationLoader.builder().setFile(file).build().save(cn);
        }
        catch (IOException e)//NOSONAR
        {
            LoggerFactory.getLogger(Reference.ID).error(Messages.ioException(file));
        }
    }

    @Override
    public void updateBuildName(SpongeBuild build, String newName)
    {
        builds.remove(build.getName());
        build.setName(newName);
        builds.put(build.getName(), build);
    }

    @Override
    public void updateBuildDescription(SpongeBuild build, List<String> description)
    {
        builds.remove(build.getName());
        build.setDescription(description);
        builds.put(build.getName(), build);
    }

    @Override
    public void updateBuildResourcePack(SpongeBuild build, String resourcePack)
    {
        builds.remove(build.getName());
        build.setResourcePack(resourcePack);
        builds.put(build.getName(), build);
    }

    @SuppressWarnings("WeakerAccess")
    public static SpongeSubmitter of(GameProfile profile)
    {
        return new SpongeSubmitter(profile);
    }

    @SuppressWarnings("WeakerAccess")
    public static SpongeSubmitter of(UUID uuid, ConfigurationNode cn)
    {
        return new SpongeSubmitter(uuid, cn);
    }
}
