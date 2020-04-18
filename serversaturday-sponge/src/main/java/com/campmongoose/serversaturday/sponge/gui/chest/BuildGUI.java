package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.gui.SpongeIconBuilder;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public abstract class BuildGUI extends SpongeChestGUI {

    protected BuildGUI(@Nonnull SpongeBuild build, int featureSlot, int teleportSlot, @Nonnull Player player) {
        super(player, Text.of(build.getName()), 9);
        Location<World> location = build.getLocation();
        setButton(teleportSlot, SpongeIconBuilder.builder(ItemTypes.COMPASS).name(Text.of(MenuText.TELEPORT_NAME)).description(MenuText.teleportDesc(location.getExtent().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()).stream().map(Text::of).collect(Collectors.toList())).build(), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> {
            if (p.hasPermission(Permissions.VIEW_GOTO)) {
                p.setLocation(location);
                p.sendMessage(Text.of(TextColors.GREEN, Messages.teleportedToBuild(build)));
                return;
            }

            p.sendMessage(Text.of(TextColors.RED, Messages.NO_PERMISSION));
        }));

        updateFeatured(build, featureSlot);
    }

    private void updateFeatured(@Nonnull SpongeBuild build, int featureSlot) {
        if (player.hasPermission(Permissions.FEATURE)) {
            List<Text> lore = Stream.concat(Stream.of(Text.of(TextColors.GOLD, "Has been featured? ", (build.featured() ? Text.of(TextColors.GREEN, "Yes") : Text.of(TextColors.RED, "No")))), MenuText.FEATURE_DESC.stream().map(Text::of)).collect(Collectors.toList());
            setButton(featureSlot, SpongeIconBuilder.builder(ItemTypes.GOLDEN_APPLE).name(Text.of(TextColors.RESET, MenuText.FEATURE_NAME)).description(lore).build(), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> {
                build.setFeatured(!build.featured());
                updateFeatured(build, featureSlot);
            }));
        }
    }
}
