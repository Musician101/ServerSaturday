package com.campmongoose.serversaturday.spigot.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.spigot.SpigotDescriptionChangeHandler;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotChestMenu;
import com.campmongoose.serversaturday.spigot.menu.anvil.NameChangeMenu;
import com.campmongoose.serversaturday.spigot.menu.anvil.ResourcePackChangeMenu;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import static com.campmongoose.serversaturday.spigot.ReflectionUtils.getInventoryClass;
import static com.campmongoose.serversaturday.spigot.ReflectionUtils.getMethod;
import static com.campmongoose.serversaturday.spigot.ReflectionUtils.getNMSClass;
import static com.campmongoose.serversaturday.spigot.ReflectionUtils.invokeMethod;

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
                    player -> new NameChangeMenu(build, submitter, player, this));
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
                    player -> new ResourcePackChangeMenu(build, submitter, player, this));
            ItemStack submit = createItem(Material.FLINT_AND_STEEL, MenuText.SUBMIT_UNREADY_NAME, MenuText.SUBMIT_UNREADY_DESC.toArray(new String[2]));
            if (build.submitted()) {
                ItemMeta meta = submit.getItemMeta();
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            set(4, submit, player -> {
                build.setSubmitted(!build.submitted());
                new BuildMenu(build, submitter, player, prevMenu);
            });
            set(5, createItem(Material.COMPASS, MenuText.TELEPORT_NAME, MenuText.teleportDesc(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ())),
                    player -> {
                        if (player.hasPermission(Permissions.VIEW_GOTO)) {
                            player.teleport(location);
                            player.sendMessage(ChatColor.GREEN + Messages.teleportedToBuild(build));
                            return;
                        }

                        player.sendMessage(ChatColor.RED + Messages.NO_PERMISSION);
                    });
            set(6, createItem(Material.BARRIER, MenuText.DELETE_NAME, MenuText.DELETE_DESC.toArray(new String[2])),
                    player -> {
                        submitter.removeBuild(build.getName());
                        if (prevMenu == null) {
                            close();
                        }
                        else {
                            prevMenu.open();
                        }
                    });
            if (player.hasPermission(Permissions.FEATURE)) {
                ItemStack featured = createItem(Material.GOLDEN_APPLE, MenuText.FEATURE_NAME, MenuText.FEATURE_DESC.toArray(new String[2]));
                if (build.featured()) {
                    featured.setDurability((short) 1);
                }

                set(7, featured, player -> {
                    build.setFeatured(!build.featured());
                    new BuildMenu(build, submitter, player, prevMenu);
                });
            }

            set(8, createItem(Material.ARROW, MenuText.BACK), player -> {
                if (prevMenu == null) {
                    close();
                }
                else {
                    prevMenu.open();
                }
            });
        }
        else {
            set(0, createItem(Material.COMPASS, MenuText.TELEPORT_NAME, MenuText.teleportDesc(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ())),
                    player -> {
                        if (player.hasPermission(Permissions.VIEW_GOTO)) {
                            player.teleport(location);
                            player.sendMessage(ChatColor.RED + Messages.teleportedToBuild(build));
                        }
                        else {
                            player.sendMessage(ChatColor.RED + Messages.NO_PERMISSION);
                        }
                    });
            set(1, createItem(Material.BOOK, MenuText.DESCRIPTION_NAME),
                    player -> {
                        try {
                            Object entityHuman = getHandle.invoke(player);
                            Class<?> nmsItemStack = getNMSClass("ItemStack");
                            Class<?> enumHand = getNMSClass("EnumHand");
                            Object mainHand = null;
                            for (Object o : enumHand.getEnumConstants()) {
                                if (o.toString().equals("MAIN_HAND")) {
                                    mainHand = o;
                                }
                            }

                            if (mainHand == null) {
                                throw new NullPointerException();
                            }

                            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                            BookMeta bookMeta = (BookMeta) book.getItemMeta();
                            bookMeta.setAuthor(submitter.getName());
                            bookMeta.setPages(build.getDescription());
                            bookMeta.setTitle(build.getName());
                            book.setItemMeta(bookMeta);
                            ItemStack old = player.getInventory().getItemInMainHand();
                            player.getInventory().setItemInMainHand(book);
                            Method openBook = getMethod(entityHuman.getClass(), "a", nmsItemStack, enumHand);
                            Method asNMSCopy = getMethod(getInventoryClass("CraftItemStack"), "asNMSCopy", ItemStack.class);
                            invokeMethod(openBook, entityHuman, invokeMethod(asNMSCopy, null, book), mainHand);
                            player.getInventory().setItemInMainHand(old);
                        }
                        catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NullPointerException e) {
                            player.sendMessage(ChatColor.RED + Messages.PREFIX + "An error occurred while attempting this operation.");
                            e.printStackTrace();
                        }
                    });
            set(2, createItem(Material.PAINTING, MenuText.RESOURCE_PACK_NAME, build.getResourcePack()));
            if (player.hasPermission(Permissions.FEATURE)) {
                ItemStack feature = createItem(Material.GOLDEN_APPLE, MenuText.FEATURE_NAME, MenuText.FEATURE_DESC.toArray(new String[2]));
                if (build.featured()) {
                    feature.setDurability((short) 1);
                }

                set(3, feature, player -> {
                    build.setFeatured(!build.featured());
                    new BuildMenu(build, submitter, player, prevMenu);
                });
            }

            set(8, createItem(Material.ARROW, MenuText.BACK), player -> {
                if (prevMenu == null) {
                    close();
                }
                else {
                    prevMenu.open();
                }
            });
        }
    }

    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        if (lore != null && lore.length > 0) {
            meta.setLore(Arrays.asList(lore));
        }

        return itemStack;
    }
}
