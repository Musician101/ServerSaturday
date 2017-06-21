package com.campmongoose.serversaturday.sponge.gui.chest.build;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.gui.chest.AbstractSpongeChestGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.GoldenApples;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public abstract class BuildGUI extends AbstractSpongeChestGUI {

    protected final SpongeBuild build;
    protected final SpongeSubmitter submitter;

    public BuildGUI(@Nonnull SpongeBuild build, @Nonnull SpongeSubmitter submitter, Player player, @Nullable AbstractSpongeChestGUI prevMenu) {
        this(build, submitter, player, prevMenu, false);
    }

    public BuildGUI(@Nonnull SpongeBuild build, @Nonnull SpongeSubmitter submitter, @Nonnull Player player, @Nullable AbstractSpongeChestGUI prevMenu, boolean manualOpen) {
        super(build.getName(), 9, player, prevMenu, manualOpen);
        this.build = build;
        this.submitter = submitter;
    }

    protected void setFeatureButton(int slot) {
        if (player.hasPermission(Permissions.FEATURE)) {
            ItemStack featured = createItem(ItemTypes.GOLDEN_APPLE, Text.of(MenuText.FEATURE_NAME), MenuText.FEATURE_DESC.stream().map(Text::of).toArray(Text[]::new));
            if (build.featured()) {
                featured.offer(Keys.GOLDEN_APPLE_TYPE, GoldenApples.ENCHANTED_GOLDEN_APPLE);
            }

            set(slot, featured, player -> {
                build.setFeatured(!build.featured());
                open();
            });
        }
    }

    protected void setTeleportButton(int slot, Location<World> location) {
        set(slot, createItem(ItemTypes.COMPASS, Text.of(MenuText.TELEPORT_NAME), Stream.of(MenuText.teleportDesc(location.getExtent().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ())).map(Text::of).toArray(Text[]::new)),
                player -> {
                    if (player.hasPermission(Permissions.VIEW_GOTO)) {
                        player.setLocation(build.getLocation());
                        player.sendMessage(Text.builder(Messages.teleportedToBuild(build)).color(TextColors.GOLD).build());
                        return;
                    }

                    player.sendMessage(Text.builder(Messages.NO_PERMISSION).color(TextColors.RED).build());
                });
    }
}
