package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.campmongoose.serversaturday.util.UUIDCacheException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class DescriptionChangeHandler implements Listener {

    private final Map<UUID, Build> builds = new HashMap<>();
    private final Map<UUID, ItemStack> itemStacks = new HashMap<>();
    private final Map<UUID, Integer> taskIds = new HashMap<>();

    public DescriptionChangeHandler() {
        Bukkit.getServer().getPluginManager().registerEvents(this, ServerSaturday.instance());
    }

    public void add(Player player, Build build) {
        UUID uuid = player.getUniqueId();
        builds.put(uuid, build);
        itemStacks.put(uuid, player.getInventory().getItemInMainHand());
        player.getInventory().setItemInMainHand(getBook(player, build));
        taskIds.put(uuid, Bukkit.getScheduler().runTaskLater(ServerSaturday.instance(), () -> remove(player), 100).getTaskId());
    }

    @EventHandler
    public void bookInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!containsPlayer(uuid)) {
            return;
        }

        if (!event.hasItem()) {
            return;
        }

        ItemStack itemStack = event.getItem();
        if (itemStack.getType() != Material.BOOK_AND_QUILL) {
            return;
        }

        if (!isSameBook((BookMeta) itemStack.getItemMeta(), player, builds.get(uuid))) {
            return;
        }

        Bukkit.getScheduler().cancelTask(taskIds.remove(uuid));
    }

    @EventHandler
    public void clickBook(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) {
            return;
        }

        if (itemStack.getType() != Material.BOOK_AND_QUILL) {
            return;
        }

        if (!isSameBook((BookMeta) itemStack.getItemMeta(), player, builds.get(uuid))) {
            return;
        }

        event.setCancelled(true);
    }

    public boolean containsPlayer(UUID uuid) {
        return builds.containsKey(uuid) || itemStacks.containsKey(uuid) || taskIds.containsKey(uuid);
    }

    @EventHandler
    public void dropBook(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack itemStack = event.getItemDrop().getItemStack();
        if (itemStack.getType() != Material.BOOK_AND_QUILL) {
            return;
        }

        if (!isSameBook((BookMeta) itemStack.getItemMeta(), player, builds.get(uuid))) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void editBook(PlayerEditBookEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!containsPlayer(uuid)) {
            return;
        }

        Build build = builds.get(uuid);
        if (!isSameBook(event.getPreviousBookMeta(), player, build)) {
            return;
        }

        Submitter submitter;
        try {
            submitter = ServerSaturday.instance().getSubmissions().getSubmitter(uuid);
            submitter.updateBuildDescription(build, event.getNewBookMeta().getPages());
            build.openMenu(submitter, player);
        }
        catch (UUIDCacheException e) {
            player.sendMessage(e.getMessage());
        }

        remove(player);
    }

    private ItemStack getBook(Player player, Build build) {
        return new ItemStack(Material.BOOK_AND_QUILL) {

            {
                BookMeta bookMeta = (BookMeta) getItemMeta();
                bookMeta.setAuthor(player.getName());
                bookMeta.setDisplayName(build.getName());
                bookMeta.setLore(Collections.singletonList("\\_o<"));
                bookMeta.setPages(build.getDescription());
                setItemMeta(bookMeta);
            }
        };
    }

    private boolean isSameBook(BookMeta book, Player player, Build build) {
        return book.equals(getBook(player, build).getItemMeta());
    }

    private void remove(Player player) {
        UUID uuid = player.getUniqueId();
        player.getInventory().setItemInMainHand(null);
        if (itemStacks.containsKey(uuid)) {
            ItemStack itemStack = itemStacks.remove(uuid);
            if (itemStack.getType() != Material.AIR) {
                player.getWorld().dropItem(player.getLocation(), itemStack);
            }
        }

        if (builds.containsKey(uuid)) {
            builds.remove(uuid);
        }

        if (builds.containsKey(uuid)) {
            Bukkit.getScheduler().cancelTask(taskIds.remove(uuid));
        }
    }
}
