package com.campmongoose.serversaturday.sponge.submission;

import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.SimpleConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.SkullTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.GameProfileManager;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SpongeSubmitter extends Submitter<SpongeBuild, ItemStack, Location<World>, SpongeSubmitter> {

    public SpongeSubmitter(@Nonnull Player player) {
        super(player.getName(), player.getUniqueId());
    }

    public SpongeSubmitter(@Nonnull UUID uuid, @Nonnull ConfigurationNode cn) {
        super(getName(uuid, cn), uuid);
        ConfigurationNode buildsCN = cn.getNode(Config.BUILDS);
        //Backwards compat for old save formats
        if (buildsCN.hasMapChildren()) {
            buildsCN.getChildrenMap().keySet().stream().filter(buildName -> !buildName.toString().contains(".")).forEach(buildName -> builds.put(buildName.toString(), new SpongeBuild(buildName.toString(), buildsCN.getNode(buildName))));
        }
        else {
            buildsCN.getChildrenList().forEach(buildCN -> {
                String name = buildCN.getNode(Config.NAME).getString();
                builds.put(name, new SpongeBuild(name, buildCN));
            });
        }
    }

    private static String getName(UUID uuid, ConfigurationNode cn) {
        return Sponge.getServer().getGameProfileManager().getCache().getById(uuid).map(gp -> gp.getName().orElse(cn.getNode(Config.NAME).getString())).orElse(cn.getNode(Config.NAME).getString());
    }

    @Nonnull
    @Override
    public ItemStack getMenuRepresentation() {
        ItemStack itemStack = ItemStack.of(ItemTypes.SKULL, 1);
        itemStack.offer(Keys.SKULL_TYPE, SkullTypes.PLAYER);
        GameProfileManager gpm = Sponge.getServer().getGameProfileManager();
        GameProfile gp = gpm.getCache().getById(uuid).orElse(gpm.createProfile(uuid, name));
        itemStack.offer(Keys.REPRESENTED_PLAYER, gp);
        itemStack.offer(Keys.DISPLAY_NAME, Text.of(gp.getName().orElse(name)));
        boolean hasNonFeaturedBuilds = false;
        for (SpongeBuild build : builds.values()) {
            if (build.submitted() && !build.featured()) {
                hasNonFeaturedBuilds = true;
            }
        }

        if (hasNonFeaturedBuilds) {
            itemStack.offer(Keys.ITEM_ENCHANTMENTS, Collections.singletonList(Enchantment.of(EnchantmentTypes.AQUA_AFFINITY, 1)));
            itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
        }

        return itemStack;
    }

    @Nonnull
    @Override
    public SpongeBuild newBuild(@Nonnull String name, @Nonnull Location<World> location) {
        builds.putIfAbsent(name, new SpongeBuild(name, location));
        return builds.get(name);
    }

    @Override
    public void save(@Nonnull File file) {
        SpongeServerSaturday.instance().map(ServerSaturday::getLogger).ifPresent(logger -> {
            try {
                if (file.createNewFile()) {
                    logger.info(Messages.newFile(file));
                }
            }
            catch (IOException e) {
                logger.error(Messages.ioException(file));
                return;
            }

            ConfigurationNode cn = SimpleConfigurationNode.root();
            cn.getNode(Config.NAME).setValue(Sponge.getServer().getGameProfileManager().getCache().getById(uuid).map(gp -> gp.getName().orElse(name)).orElse(name));
            List<Map<String, Object>> buildsMap = new ArrayList<>();
            builds.forEach((key, value) -> buildsMap.add(value.serialize()));
            cn.getNode(Config.BUILDS).setValue(buildsMap);
            try {
                HoconConfigurationLoader.builder().setFile(file).build().save(cn);
            }
            catch (IOException e) {
                logger.error(Messages.ioException(file));
            }
        });
    }
}
