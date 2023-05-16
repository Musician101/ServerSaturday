package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.Reference.MenuText;
import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.Reference.Permissions;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import static io.musician101.musigui.spigot.chest.SpigotIconUtil.customName;
import static io.musician101.musigui.spigot.chest.SpigotIconUtil.setLore;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public abstract class BuildGUI extends ServerSaturdayChestGUI {

    protected BuildGUI(@Nonnull Build build, @Nonnull Submitter submitter, int featureSlot, int teleportSlot, @Nonnull Player player) {
        super(player, build.getName(), 9);
        Location location = build.getLocation();
        List<String> lore = MenuText.teleportDesc(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        ItemStack itemStack = setLore(customName(new ItemStack(Material.COMPASS), MenuText.TELEPORT_NAME), lore);
        setButton(teleportSlot, itemStack, ClickType.LEFT, p -> {
            if (p.hasPermission(Permissions.VIEW_GOTO)) {
                p.teleport(location);
                p.sendMessage(text(Messages.teleportedToBuild(build), GREEN));
                return;
            }

            p.sendMessage(Messages.NO_PERMISSION);
        });

        updateFeatured(build, submitter, featureSlot);
    }

    private void updateFeatured(@Nonnull Build build, @Nonnull Submitter submitter, int featureSlot) {
        if (player.hasPermission(Permissions.FEATURE)) {
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "Has been featured? " + (build.featured() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
            lore.addAll(MenuText.FEATURE_DESC);
            setButton(featureSlot, setLore(customName(new ItemStack(Material.GOLDEN_APPLE), ChatColor.RESET + MenuText.FEATURE_NAME), lore), ClickType.LEFT, p -> {
                build.setFeatured(!build.featured());
                if (build.featured()) {
                    ServerSaturday.getInstance().getRewardHandler().giveReward(Bukkit.getOfflinePlayer(submitter.getUUID()));
                }

                updateFeatured(build, submitter, featureSlot);
            });
        }
    }
}
