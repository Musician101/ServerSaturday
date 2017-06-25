package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.SubmissionsNotLoadedException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class DescriptionChangeHandler implements Listener {

    private final Map<UUID, Build> builds = new HashMap<>();
    private final Map<UUID, Integer> bookSlots = new HashMap<>();

    public DescriptionChangeHandler() {
        Bukkit.getServer().getPluginManager().registerEvents(this, ServerSaturday.instance());
    }

    public void add(Player player, Build build) {
        UUID uuid = player.getUniqueId();
        builds.put(uuid, build);
        bookSlots.put(uuid, player.getInventory().getHeldItemSlot());
        ItemStack itemStack = new ItemStack(Material.BOOK_AND_QUILL);
        BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
        bookMeta.setAuthor(player.getName());
        bookMeta.setDisplayName(build.getName());
        bookMeta.setLore(Collections.singletonList("\\_o<"));
        bookMeta.setPages(build.getDescription());
        itemStack.setItemMeta(bookMeta);
        player.getInventory().setItemInMainHand(itemStack);
    }

    private boolean check(Player player, ItemStack itemStack) {
        if (builds.containsKey(player.getUniqueId()) && itemStack != null && (itemStack.getType() == Material.BOOK_AND_QUILL || itemStack.getType() == Material.WRITTEN_BOOK) && itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta.hasLore()) {
                List<String> lore = itemMeta.getLore();
                if (!lore.isEmpty() && lore.get(0).equals("\\_o<")) {
                    return true;
                }
            }
        }

        return false;
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        UUID uuid = player.getUniqueId();
        int slot = bookSlots.getOrDefault(uuid, 0);
        if (check(player, inv.getItem(slot))) {
            inv.setItem(slot, null);
            remove(player);
        }
    }

    @EventHandler
    public void clickBook(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            ItemStack itemStack = event.getCurrentItem();
            if (check(player, itemStack)) {
                event.setCancelled(true);
            }
        }
    }

    public boolean containsPlayer(UUID uuid) {
        return builds.containsKey(uuid) && bookSlots.containsKey(uuid);
    }

    @EventHandler
    public void dropBook(PlayerDropItemEvent event) {
        if (check(event.getPlayer(), event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void editBook(PlayerEditBookEvent event) {
        BookMeta oldMeta = event.getPreviousBookMeta();
        if (oldMeta.hasLore() && oldMeta.getLore().contains("\\_o<")) {
            ItemStack itemStack = new ItemStack(Material.BOOK_AND_QUILL);
            BookMeta meta = event.getNewBookMeta();
            meta.setLore(oldMeta.getLore());
            itemStack.setItemMeta(meta);
            Player player = event.getPlayer();
            if (check(player, itemStack)) {
                UUID uuid = player.getUniqueId();
                Build build = builds.get(uuid);
                try {
                    build.setDescription(meta.getPages());
                    build.openMenu(ServerSaturday.instance().getSubmissions().getSubmitter(uuid), player);
                    player.sendMessage(ChatColor.GOLD + Reference.PREFIX + build.getName() + "'s description has been updated.");
                }
                catch (SubmissionsNotLoadedException e) {
                    player.sendMessage(e.getMessage());
                }

                remove(player);
            }
        }
    }

    private void remove(Player player) {
        UUID uuid = player.getUniqueId();
        player.getInventory().setItem(bookSlots.getOrDefault(uuid, 0), null);
        builds.remove(uuid);
        bookSlots.remove(uuid);
    }
}
