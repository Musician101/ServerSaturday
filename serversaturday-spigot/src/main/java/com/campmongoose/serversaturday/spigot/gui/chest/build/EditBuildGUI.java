package com.campmongoose.serversaturday.spigot.gui.chest.build;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.gui.anvil.SSAnvilGUI;
import com.campmongoose.serversaturday.spigot.gui.book.BookGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.AbstractSpigotChestGUI;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EditBuildGUI extends BuildGUI {

    public EditBuildGUI(@Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter, @Nonnull Player player, @Nullable AbstractSpigotChestGUI prevMenu) {
        super(build, submitter, player, prevMenu, true);
        if (submitter.getUUID().equals(player.getUniqueId())) {
            open();
        }
    }

    @Override
    protected void build() {
        set(0, createItem(Material.PAPER, MenuText.RENAME_NAME, MenuText.RENAME_DESC),
                player -> new SSAnvilGUI(player, (p, s) -> {
                    build.setName(s);
                    open();
                    return null;
                }));
        set(1, createItem(Material.COMPASS, MenuText.CHANGE_LOCATION_NAME, MenuText.CHANGE_LOCATION_DESC.toArray(new String[3])),
                player -> {
                    build.setLocation(player.getLocation());
                    player.closeInventory();
                    open();
                    player.sendMessage(ChatColor.GREEN + Messages.locationChanged(build));
                });
        set(2, createItem(Material.BOOK, MenuText.CHANGE_DESCRIPTION_NAME, MenuText.CHANGE_DESCRIPTION_DESC),
                player -> {
                    if (BookGUI.isEditing(player)) {
                        player.sendMessage(ChatColor.RED + Messages.EDIT_IN_PROGRESS);
                        return;
                    }

                    player.closeInventory();
                    new BookGUI(player, build, pages -> {
                        build.setDescription(pages);
                        player.sendMessage(ChatColor.GOLD + Messages.PREFIX + build.getName() + "'s description has been updated.");
                        open();
                    });
                });
        set(3, createItem(Material.PAINTING, MenuText.CHANGE_RESOURCE_PACK_NAME, MenuText.CHANGE_RESOURCE_PACK_DESC.toArray(new String[2])),
                player -> new BookGUI(player, build, pages -> {
                    build.setResourcePack(pages);
                    player.closeInventory();
                    open();
                }));

        ItemStack submit = createItem(Material.FLINT_AND_STEEL, MenuText.SUBMIT_UNREADY_NAME, MenuText.SUBMIT_UNREADY_DESC.toArray(new String[2]));
        if (build.submitted()) {
            ItemMeta meta = submit.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            submit.setItemMeta(meta);
        }

        set(4, submit, player -> {
            build.setSubmitted(!build.submitted());
            open();
        });
        setTeleportButton(5, build.getLocation());
        set(6, createItem(Material.BARRIER, MenuText.DELETE_NAME, MenuText.DELETE_DESC.toArray(new String[2])),
                player -> {
                    submitter.removeBuild(build.getName());
                    player.closeInventory();
                    if (prevMenu != null) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotServerSaturday.instance(), prevMenu::open);
                    }
                });
        setFeatureButton(7);
        setBackButton(8, Material.ARROW);
    }
}
