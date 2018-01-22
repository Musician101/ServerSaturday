package com.campmongoose.serversaturday.spigot.gui;

import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.minecraft.server.v1_12_R1.EnumHand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class SpigotBookGUI implements Listener {

    private static final String LORE_IDENTIFIER = "\\_o<";
    private static final Predicate<ItemStack> BOOK_FILTER = itemStack -> {
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
    };
    private final int bookSlot;
    private final Consumer<List<String>> consumer;
    private final Player player;

    public SpigotBookGUI(Player player, SpigotBuild build, List<String> originalPages, Consumer<List<String>> action) {
        this.player = player;
        this.bookSlot = player.getInventory().getHeldItemSlot();
        this.consumer = action;
        ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setAuthor(player.getName());
        bookMeta.setDisplayName(build.getName());
        ItemMeta meta = book.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(LORE_IDENTIFIER);
        bookMeta.setPages(originalPages);
        meta.setLore(lore);
        book.setItemMeta(meta);
        player.getInventory().setItemInMainHand(book);
        Bukkit.getServer().getPluginManager().registerEvents(this, (SpigotServerSaturday) SpigotServerSaturday.instance());
    }

    public static boolean isEditing(Player player) {
        return !Stream.of(player.getInventory().getContents()).filter(BOOK_FILTER).collect(Collectors.toList()).isEmpty();
    }

    @EventHandler
    public void clickBook(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            event.setCancelled(isEditing((Player) event.getWhoClicked(), event.getCurrentItem()));
        }
    }

    @EventHandler
    public void dropBook(PlayerDropItemEvent event) {
        event.setCancelled(isEditing(event.getPlayer(), event.getItemDrop().getItemStack()));
    }

    @EventHandler
    public void editBook(PlayerEditBookEvent event) {
        BookMeta oldMeta = event.getPreviousBookMeta();
        if (oldMeta.hasLore() && oldMeta.getLore().contains(LORE_IDENTIFIER)) {
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

    private boolean isEditing(Player player, ItemStack itemStack) {
        return player.getUniqueId().equals(this.player.getUniqueId()) && itemStack != null && BOOK_FILTER.test(itemStack);
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        if (isEditing(player, inv.getItem(bookSlot))) {
            remove();
        }
    }

    private void remove() {
        player.getInventory().setItem(bookSlot, null);
        HandlerList.unregisterAll(this);
    }

    public static void openWrittenBook(@Nonnull Player player, @Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter) {
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
    }
}
