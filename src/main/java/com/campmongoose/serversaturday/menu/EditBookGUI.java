package com.campmongoose.serversaturday.menu;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
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

public class EditBookGUI implements Listener {

    private static final String ID = "\\_o<";
    private final Function<Build, List<String>> bookPageApplier;
    private final Map<UUID, Data> data = new HashMap<>();
    private final BiConsumer<Build, List<String>> listApplier;

    public EditBookGUI(Function<Build, List<String>> bookPageApplier, BiConsumer<Build, List<String>> listApplier) {
        this.bookPageApplier = bookPageApplier;
        this.listApplier = listApplier;
        Bukkit.getServer().getPluginManager().registerEvents(this, ServerSaturday.instance());
    }

    public void add(Player player, Build build) {
        UUID uuid = player.getUniqueId();
        data.put(uuid, new Data(player.getInventory().getHeldItemSlot(), build));
        ItemStack itemStack = new ItemStack(Material.BOOK_AND_QUILL);
        BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
        bookMeta.setAuthor(player.getName());
        bookMeta.setDisplayName(build.getName());
        bookMeta.setLore(Collections.singletonList("\\_o<"));
        bookMeta.setPages(bookPageApplier.apply(build));
        itemStack.setItemMeta(bookMeta);
        player.getInventory().setItemInMainHand(itemStack);
    }

    protected boolean check(Player player, ItemStack itemStack) {
        if (!data.containsKey(player.getUniqueId())) {
            return false;
        }

        if (itemStack == null) {
            return false;
        }

        Material material = itemStack.getType();
        if (material != Material.BOOK_AND_QUILL && material != Material.WRITTEN_BOOK) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!itemMeta.hasLore()) {
            return false;
        }

        List<String> lore = itemMeta.getLore();
        return !lore.isEmpty() && lore.get(0).equals(ID);
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

    public boolean containsPlayer(Player player) {
        return data.containsKey(player.getUniqueId());
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
        if (!oldMeta.hasLore() && !oldMeta.getLore().contains(ID)) {
            return;
        }

        ItemStack itemStack = new ItemStack(Material.BOOK_AND_QUILL);
        BookMeta meta = event.getNewBookMeta();
        meta.setLore(oldMeta.getLore());
        itemStack.setItemMeta(meta);
        Player player = event.getPlayer();
        if (!check(player, itemStack)) {
            return;
        }

        UUID uuid = player.getUniqueId();
        Build build = data.get(uuid).build;
        listApplier.accept(build, ((BookMeta) itemStack.getItemMeta()).getPages());
        build.openMenu(ServerSaturday.instance().getSubmissions().getSubmitter(uuid), player);
        player.sendMessage(ChatColor.GOLD + Reference.PREFIX + build.getName() + "'s description has been updated.");
        remove(player);
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        int slot = data.getOrDefault(player.getUniqueId(), new Data(0, null)).bookSlot;
        if (check(player, inv.getItem(slot))) {
            inv.setItem(slot, null);
            remove(player);
        }
    }

    private void remove(Player player) {
        UUID uuid = player.getUniqueId();
        if (data.containsKey(uuid)) {
            player.getInventory().setItem(data.get(uuid).bookSlot, null);
            data.remove(player.getUniqueId());
        }
    }

    private static class Data {

        final int bookSlot;
        final Build build;

        public Data(int bookSlot, Build build) {
            this.bookSlot = bookSlot;
            this.build = build;
        }
    }
}
