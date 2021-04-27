package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.submission.Build;
import com.google.common.collect.ImmutableMap;
import io.musician101.musicianlibrary.java.minecraft.sponge.gui.chest.SpongeIconBuilder;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.menu.ClickTypes;
import org.spongepowered.api.world.server.ServerLocation;

public abstract class BuildGUI extends SpongeServerSaturdayChestGUI {

    protected BuildGUI(@Nonnull Build<Component> build, int featureSlot, int teleportSlot, @Nonnull ServerPlayer player) {
        super(player, Component.text(build.getName()), 9);
        ServerLocation location = toSpongeLocation(build.getLocation());
        setButton(teleportSlot, SpongeIconBuilder.builder(ItemTypes.COMPASS).name(Component.text(MenuText.TELEPORT_NAME)).description(MenuText.teleportDesc(location.worldKey().asString(), location.blockX(), location.blockY(), location.blockZ()).stream().map(Component::text).collect(Collectors.toList())).build(), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> {
            if (p.hasPermission(Permissions.VIEW_GOTO)) {
                p.setLocation(location);
                p.sendMessage(Component.text(Messages.teleportedToBuild(build)).color(NamedTextColor.GREEN));
                return;
            }

            p.sendMessage(Component.text(Messages.NO_PERMISSION).color(NamedTextColor.RED));
        }));

        updateFeatured(build, featureSlot);
    }

    private ServerLocation toSpongeLocation(io.musician101.musicianlibrary.java.minecraft.common.Location location) {
        return ServerLocation.of(Sponge.server().worldManager().world(ResourceKey.resolve(location.getWorldName())).get(), location.getX(), location.getY(), location.getZ());
    }

    protected io.musician101.musicianlibrary.java.minecraft.common.Location toLibLocation(ServerLocation location) {
        return new io.musician101.musicianlibrary.java.minecraft.common.Location(location.worldKey().asString(), location.x(), location.y(), location.z());
    }

    private void updateFeatured(@Nonnull Build<Component> build, int featureSlot) {
        if (player.hasPermission(Permissions.FEATURE)) {
            List<Component> lore = Stream.concat(Stream.of(Component.join(Component.text(" "), Component.text("Has been featured?").color(NamedTextColor.GOLD), (build.featured() ? Component.text("Yes").color(NamedTextColor.GREEN) : Component.text("No").color(NamedTextColor.RED)))), MenuText.FEATURE_DESC.stream().map(Component::text)).collect(Collectors.toList());
            setButton(featureSlot, SpongeIconBuilder.builder(ItemTypes.GOLDEN_APPLE).name(Component.text(MenuText.FEATURE_NAME).color(NamedTextColor.WHITE)).description(lore).build(), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> {
                build.setFeatured(!build.featured());
                updateFeatured(build, featureSlot);
            }));
        }
    }
}
