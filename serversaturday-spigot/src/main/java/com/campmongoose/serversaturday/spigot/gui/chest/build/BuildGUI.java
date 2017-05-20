package com.campmongoose.serversaturday.spigot.gui.chest.build;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.spigot.gui.chest.AbstractSpigotChestGUI;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class BuildGUI extends AbstractSpigotChestGUI {

    protected final SpigotBuild build;
    protected final SpigotSubmitter submitter;

    public BuildGUI(@Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter, @Nonnull Player player, @Nullable AbstractSpigotChestGUI prevMenu) {
        this(build, submitter, player, prevMenu, false);
    }

    public BuildGUI(@Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter, @Nonnull Player player, @Nullable AbstractSpigotChestGUI prevMenu, boolean manualOpen) {
        super(Bukkit.createInventory(player, 9, build.getName()), player, prevMenu, manualOpen);
        this.build = build;
        this.submitter = submitter;
    }

    protected void setFeatureButton(int slot) {
        if (player.hasPermission(Permissions.FEATURE)) {
            ItemStack featured = createItem(Material.GOLDEN_APPLE, MenuText.FEATURE_NAME, MenuText.FEATURE_DESC.toArray(new String[0]));
            if (build.featured()) {
                featured.setDurability((short) 1);
            }

            set(slot, featured, player -> {
                build.setFeatured(!build.featured());
                open();
            });
        }
    }

    protected void setTeleportButton(int slot, Location location) {
        set(slot, createItem(Material.COMPASS, MenuText.TELEPORT_NAME, MenuText.teleportDesc(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()).toArray(new String[0])),
                player -> {
                    if (player.hasPermission(Permissions.VIEW_GOTO)) {
                        player.teleport(location);
                        player.sendMessage(ChatColor.GREEN + Messages.teleportedToBuild(build));
                        return;
                    }

                    player.sendMessage(ChatColor.RED + Messages.NO_PERMISSION);
                });
    }
}
