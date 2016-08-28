package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.AbstractDescriptionChangeHandler;
import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class SpigotDescriptionChangeHandler extends AbstractDescriptionChangeHandler<SpigotBuild, InventoryClickEvent, PlayerDropItemEvent, PlayerInteractEvent, ItemStack, BookMeta, Integer, PlayerEditBookEvent> implements Listener
{
    @SuppressWarnings("WeakerAccess")
    public SpigotDescriptionChangeHandler()
    {
        super();
        SpigotServerSaturday.getInstance().getServer().getPluginManager().registerEvents(this, SpigotServerSaturday.getInstance());
    }

    @Override
    protected ItemStack getBook(UUID uuid, SpigotBuild build)
    {
        ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setAuthor(Bukkit.getPlayer(uuid).getName());
        bookMeta.setDisplayName(build.getName());
        bookMeta.setLore(Collections.singletonList(Reference.DUCK));
        bookMeta.setPages(build.getDescription());
        book.setItemMeta(bookMeta);
        return book;
    }

    @Override
    public void add(UUID uuid, SpigotBuild build)
    {
        Player player = Bukkit.getPlayer(uuid);
        builds.put(uuid, build);
        itemStacks.put(uuid, player.getInventory().getItemInMainHand());
        player.getInventory().setItemInMainHand(getBook(uuid, build));
        taskIds.put(uuid, new BukkitRunnable()
        {
            @Override
            public void run()
            {
                player.getInventory().setItemInMainHand(itemStacks.get(uuid));
            }
        }.runTaskLater(SpigotServerSaturday.getInstance(), 100).getTaskId());
    }

    @Override
    protected void remove(UUID uuid)
    {
        Player player = Bukkit.getPlayer(uuid);
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

    @Override
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean isSameBook(BookMeta book, UUID uuid, SpigotBuild build)
    {
        return book.equals(getBook(uuid, build).getItemMeta());
    }

    @Override
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

        if (!isSameBook((BookMeta) itemStack.getItemMeta(), uuid, builds.get(uuid)))
            return;

        Bukkit.getScheduler().cancelTask(taskIds.remove(uuid));
    }

    @Override
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

        if (!isSameBook((BookMeta) itemStack.getItemMeta(), uuid, builds.get(uuid)))
            return;

        event.setCancelled(true);
    }

    @Override
    @EventHandler
    public void dropBook(PlayerDropItemEvent event)
    {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack itemStack = event.getItemDrop().getItemStack();
        if (itemStack.getType() != Material.BOOK_AND_QUILL)
            return;

        if (!isSameBook((BookMeta) itemStack.getItemMeta(), uuid, builds.get(uuid)))
            return;

        event.setCancelled(true);
    }

    @Override
    @EventHandler
    public void editBook(PlayerEditBookEvent event)
    {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!containsPlayer(uuid))
            return;

        SpigotBuild build = builds.get(uuid);
        if (!isSameBook(event.getPreviousBookMeta(), uuid, build))
            return;

        SpigotSubmitter submitter = SpigotServerSaturday.getInstance().getSubmissions().getSubmitter(uuid);
        submitter.updateBuildDescription(build, event.getNewBookMeta().getPages());
        build.openMenu(submitter, player.getUniqueId());
        remove(uuid);
    }
}
