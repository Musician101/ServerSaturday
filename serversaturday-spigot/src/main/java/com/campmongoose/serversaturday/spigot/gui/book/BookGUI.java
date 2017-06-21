package com.campmongoose.serversaturday.spigot.gui.book;

import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class BookGUI implements Listener {

    private final Player player;
    private final int bookSlot;
    private static final String LORE_IDENTIFIER = "\\_o<";
    private final Consumer<List<String>> consumer;

    public BookGUI(Player player, SpigotBuild build, List<String> originalPages, Consumer<List<String>> consumer) {
        this.player = player;
        this.bookSlot = player.getInventory().getHeldItemSlot();
        this.consumer = consumer;
        ItemStack itemStack = new ItemStack(Material.BOOK_AND_QUILL);
        BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
        bookMeta.setAuthor(player.getName());
        bookMeta.setDisplayName(build.getName());
        bookMeta.setLore(Collections.singletonList(LORE_IDENTIFIER));
        bookMeta.setPages(originalPages);
        itemStack.setItemMeta(bookMeta);
        player.getInventory().setItemInMainHand(itemStack);
        Bukkit.getServer().getPluginManager().registerEvents(this, SpigotServerSaturday.instance());
    }

    public static boolean isEditing(Player player) {
        return !Stream.of(player.getInventory().getContents()).filter(itemStack -> {
            Material material = itemStack.getType();
            if (material == Material.BOOK_AND_QUILL || material == Material.WRITTEN_BOOK) {
                if (itemStack.hasItemMeta()) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (itemMeta.hasLore()) {
                        return itemMeta.getLore().contains(LORE_IDENTIFIER);
                    }
                }
            }
            return false;
        }).collect(Collectors.toList()).isEmpty();
    }

    private boolean isEditing(Player player, ItemStack itemStack) {
        if (player.getUniqueId().equals(this.player.getUniqueId()) && itemStack != null && (itemStack.getType() == Material.BOOK_AND_QUILL || itemStack.getType() == Material.WRITTEN_BOOK) && itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta.hasLore()) {
                List<String> lore = itemMeta.getLore();
                if (!lore.isEmpty() && lore.get(0).equals(LORE_IDENTIFIER)) {
                    return true;
                }
            }
        }

        return false;
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (isEditing(player, player.getInventory().getItem(bookSlot))) {
            remove();
        }
    }

    @EventHandler
    public void clickBook(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && isEditing((Player) event.getWhoClicked(), event.getCurrentItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void dropBook(PlayerDropItemEvent event) {
        if (isEditing(event.getPlayer(), event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void editBook(PlayerEditBookEvent event) {
        BookMeta oldMeta = event.getPreviousBookMeta();
        if (oldMeta.hasLore() && oldMeta.getLore().get(0).contains(LORE_IDENTIFIER)) {
            ItemStack itemStack = new ItemStack(Material.BOOK_AND_QUILL);
            BookMeta meta = event.getNewBookMeta();
            meta.setLore(oldMeta.getLore());
            itemStack.setItemMeta(meta);
            Player player = event.getPlayer();
            if (isEditing(player, itemStack)) {
                consumer.accept(meta.getPages());
                remove();
            }
        }
    }

    private void remove() {
        player.getInventory().setItem(bookSlot, null);
        HandlerList.unregisterAll(this);
    }
}
