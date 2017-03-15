package com.campmongoose.serversaturday.spigot.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.spigot.SpigotDescriptionChangeHandler;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.menu.anvil.NameChangeMenu;
import com.campmongoose.serversaturday.spigot.menu.anvil.ResourcePackChangeMenu;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.server.v1_10_R1.EnumHand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class BuildMenu extends AbstractSpigotChestMenu {

    private final SpigotBuild build;
    private final SpigotSubmitter submitter;

    public BuildMenu(@Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter, @Nonnull Player player, @Nullable AbstractSpigotChestMenu prevMenu) {
        this(build, submitter, player, prevMenu, false);
    }

    public BuildMenu(@Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter, @Nonnull Player player, @Nullable AbstractSpigotChestMenu prevMenu, boolean manualOpen) {
        super(Bukkit.createInventory(player, 9, build.getName()), player, prevMenu, manualOpen);
        this.build = build;
        this.submitter = submitter;
    }
    
    @Override
    protected void build() {
        Location location = build.getLocation();
        if (player.getUniqueId().equals(submitter.getUUID())) {
            set(0, createItem(Material.PAPER, MenuText.RENAME_NAME, MenuText.RENAME_DESC),
                    player -> new NameChangeMenu(build, submitter, player, prevMenu));
            set(1, createItem(Material.COMPASS, MenuText.CHANGE_LOCATION_NAME, MenuText.CHANGE_LOCATION_DESC.toArray(new String[3])),
                    player -> {
                        build.setLocation(player.getLocation());
                        new BuildMenu(build, submitter, player, prevMenu);
                        player.sendMessage(ChatColor.GREEN + Messages.locationChanged(build));
                    });
            set(2, createItem(Material.BOOK, MenuText.CHANGE_DESCRIPTION_NAME, MenuText.CHANGE_DESCRIPTION_DESC),
                    player -> {
                        SpigotDescriptionChangeHandler sdch = SpigotServerSaturday.instance().getDescriptionChangeHandler();
                        if (sdch.containsPlayer(player.getUniqueId())) {
                            player.sendMessage(ChatColor.RED + Messages.EDIT_IN_PROGRESS);
                            return;
                        }

                        sdch.add(player, build);
                    });
            set(3, createItem(Material.PAINTING, MenuText.CHANGE_RESOURCE_PACK_NAME, MenuText.CHANGE_RESOURCE_PACK_DESC.toArray(new String[2])),
                    player -> new ResourcePackChangeMenu(build, submitter, player, prevMenu));

            ItemStack submit = createItem(Material.FLINT_AND_STEEL, MenuText.SUBMIT_UNREADY_NAME, MenuText.SUBMIT_UNREADY_DESC.toArray(new String[2]));
            if (build.submitted()) {
                ItemMeta meta = submit.getItemMeta();
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                submit.setItemMeta(meta);
            }

            set(4, submit, player -> {
                build.setSubmitted(!build.submitted());
                new BuildMenu(build, submitter, player, prevMenu);
            });
            setTeleportButton(5, location);
            set(6, createItem(Material.BARRIER, MenuText.DELETE_NAME, MenuText.DELETE_DESC.toArray(new String[2])),
                    player -> {
                        submitter.removeBuild(build.getName());
                        player.closeInventory();
                        if (prevMenu != null) {
                            Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotServerSaturday.instance(), prevMenu::open);
                        }
                    });
            setFeatureButton(7);
        }
        else {
            setTeleportButton(0, location);
            set(1, createItem(Material.BOOK, MenuText.DESCRIPTION_NAME),
                    player -> {
                        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                        BookMeta bookMeta = (BookMeta) book.getItemMeta();
                        bookMeta.setAuthor(submitter.getName());
                        bookMeta.setPages(build.getDescription());
                        bookMeta.setTitle(build.getName());
                        book.setItemMeta(bookMeta);
                        ItemStack old = player.getInventory().getItemInMainHand();
                        player.getInventory().setItemInMainHand(book);
                        ((CraftPlayer) player).getHandle().a(CraftItemStack.asNMSCopy(book), EnumHand.MAIN_HAND);
                        player.getInventory().setItemInMainHand(old);
                    });
            set(2, createItem(Material.PAINTING, MenuText.RESOURCE_PACK_NAME, build.getResourcePack()));
            setFeatureButton(3);
        }

        setBackButton(8, Material.ARROW);
    }

    private void setFeatureButton(int slot) {
        if (player.hasPermission(Permissions.FEATURE)) {
            ItemStack featured = createItem(Material.GOLDEN_APPLE, MenuText.FEATURE_NAME, MenuText.FEATURE_DESC.toArray(new String[0]));
            if (build.featured()) {
                featured.setDurability((short) 1);
            }

            set(slot, featured, player -> {
                build.setFeatured(!build.featured());
                new BuildMenu(build, submitter, player, prevMenu);
            });
        }
    }

    private void setTeleportButton(int slot, Location location) {
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
