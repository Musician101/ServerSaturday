package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.submission.Build;
import com.google.common.collect.ImmutableMap;
import io.musician101.musicianlibrary.java.minecraft.spigot.gui.chest.SpigotIconBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public abstract class BuildGUI extends SpigotServerSaturdayChestGUI {

    protected BuildGUI(@Nonnull Build<String> build, int featureSlot, int teleportSlot, @Nonnull Player player) {
        super(player, build.getName(), 9);
        Location location = toSpigotLocation(build.getLocation());
        setButton(teleportSlot, SpigotIconBuilder.builder(Material.COMPASS).name(MenuText.TELEPORT_NAME).description(MenuText.teleportDesc(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ())).build(), ImmutableMap.of(ClickType.LEFT, p -> {
            if (p.hasPermission(Permissions.VIEW_GOTO)) {
                p.teleport(location);
                p.sendMessage(ChatColor.GREEN + Messages.teleportedToBuild(build));
                return;
            }

            p.sendMessage(ChatColor.RED + Messages.NO_PERMISSION);
        }));

        updateFeatured(build, featureSlot);
    }

    private Location toSpigotLocation(io.musician101.musicianlibrary.java.minecraft.common.Location location) {
        return new Location(Bukkit.getWorld(location.getWorldName()), location.getX(), location.getY(), location.getZ());
    }

    protected io.musician101.musicianlibrary.java.minecraft.common.Location toLibLocation(Location location) {
        return new io.musician101.musicianlibrary.java.minecraft.common.Location(location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
    }

    private void updateFeatured(@Nonnull Build<String> build, int featureSlot) {
        if (player.hasPermission(Permissions.FEATURE)) {
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "Has been featured? " + (build.featured() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
            lore.addAll(MenuText.FEATURE_DESC);
            setButton(featureSlot, SpigotIconBuilder.builder(Material.GOLDEN_APPLE).name(ChatColor.RESET + MenuText.FEATURE_NAME).description(lore).build(), ImmutableMap.of(ClickType.LEFT, p -> {
                build.setFeatured(!build.featured());
                updateFeatured(build, featureSlot);
            }));
        }
    }
}
