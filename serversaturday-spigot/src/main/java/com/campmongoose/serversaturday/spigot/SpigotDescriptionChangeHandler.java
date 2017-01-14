package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.spigot.menu.chest.BuildMenu;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
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

public class SpigotDescriptionChangeHandler implements Listener
{
    private final Map<UUID, SpigotBuild> builds = new HashMap<>();
    private final Map<UUID, ItemStack> itemStacks = new HashMap<>();
    private final Map<UUID, Integer> taskIds = new HashMap<>();
    
    public SpigotDescriptionChangeHandler()
    {
        super();
        Bukkit.getServer().getPluginManager().registerEvents(this, SpigotServerSaturday.instance());
    }

    public boolean containsPlayer(UUID uuid)
    {
        return builds.containsKey(uuid) || itemStacks.containsKey(uuid) || taskIds.containsKey(uuid);
    }
    
    private ItemStack getBook(Player player, SpigotBuild build)
    {
        ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setAuthor(player.getDisplayName());
        bookMeta.setDisplayName(build.getName());
        bookMeta.setLore(Collections.singletonList(Reference.DUCK));
        bookMeta.setPages(build.getDescription());
        book.setItemMeta(bookMeta);
        return book;
    }

    public void add(Player player, SpigotBuild build)
    {
        UUID uuid = player.getUniqueId();
        builds.put(uuid, build);
        itemStacks.put(uuid, player.getInventory().getItemInMainHand());
        player.getInventory().setItemInMainHand(getBook(player, build));
        taskIds.put(uuid, Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotServerSaturday.instance(), () -> player.getInventory().setItemInMainHand(itemStacks.get(uuid)), 100));
    }

    private void remove(Player player)
    {
        UUID uuid = player.getUniqueId();
        player.getInventory().setItemInMainHand(null);
        if (itemStacks.containsKey(uuid))
        {
            ItemStack itemStack = itemStacks.remove(uuid);
            if (itemStack.getType() != Material.AIR)
                player.getWorld().dropItem(player.getLocation(), itemStack);
        }

        if (builds.containsKey(uuid))
            builds.remove(uuid);

        if (builds.containsKey(uuid))
            Bukkit.getScheduler().cancelTask(taskIds.remove(uuid));
    }

    private boolean isSameBook(BookMeta book, Player player, SpigotBuild build)
    {
        return book.equals(getBook(player, build).getItemMeta());
    }

    @EventHandler
    public void bookInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!containsPlayer(uuid))
            return;

        if (!event.hasItem())
            return;

        ItemStack itemStack = event.getItem();
        if (itemStack.getType() != Material.BOOK_AND_QUILL)
            return;

        if (!isSameBook((BookMeta) itemStack.getItemMeta(), player, builds.get(uuid)))
            return;

        Bukkit.getScheduler().cancelTask(taskIds.remove(uuid));
    }

    @EventHandler
    public void clickBook(InventoryClickEvent event)
    {
        if (!(event.getWhoClicked() instanceof Player))
            return;

        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null)
            return;

        if (itemStack.getType() != Material.BOOK_AND_QUILL)
            return;

        if (!isSameBook((BookMeta) itemStack.getItemMeta(), player, builds.get(uuid)))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void dropBook(PlayerDropItemEvent event)
    {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack itemStack = event.getItemDrop().getItemStack();
        if (itemStack.getType() != Material.BOOK_AND_QUILL)
            return;

        if (!isSameBook((BookMeta) itemStack.getItemMeta(), player, builds.get(uuid)))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void editBook(PlayerEditBookEvent event)
    {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!containsPlayer(uuid))
            return;

        SpigotBuild build = builds.get(uuid);
        if (!isSameBook(event.getPreviousBookMeta(), player, build))
            return;

        SpigotSubmitter submitter = SpigotServerSaturday.instance().getSubmissions().getSubmitter(player);
        submitter.updateBuildDescription(build, event.getNewBookMeta().getPages());
        new BuildMenu(build, submitter, player, null);
        remove(player);
    }
}
