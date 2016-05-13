package com.campmongoose.serversaturday.spigot.menu.chest;

import com.campmongoose.serversaturday.spigot.ServerSaturday;
import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("WeakerAccess")
public class ChestMenu extends AbstractSpigotMenu<ChestMenu>
{
    ChestMenu(Inventory inv, SpigotClickEventHandler handler)
    {
        super(inv, handler);
        ServerSaturday.getInstance().getServer().getPluginManager().registerEvents(this, ServerSaturday.getInstance());
    }

    @EventHandler
    @Override
    public void onClick(InventoryClickEvent event)
    {
        if (!(event.getWhoClicked() instanceof Player))
            return;

        if (!inv.equals(event.getInventory()))
            return;

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType() == Material.AIR)
            return;

        int slot = event.getRawSlot();
        SpigotSSClickEvent clickEvent = new SpigotSSClickEvent(player, itemStack, slot);
        handler.handle(clickEvent);
        if (clickEvent.willClose())
            player.closeInventory();

        if (clickEvent.willDestroy())
            destroy(this);
    }

    @EventHandler
    @Override
    public void onClose(InventoryCloseEvent event)
    {
        if (!(event.getPlayer() instanceof Player))
            return;

        Inventory inv = event.getInventory();
        if (inv.equals(this.inv))
            destroy(this);
    }

    @EventHandler
    @Override
    public void onQuit(PlayerQuitEvent event)
    {
        if (inv.equals(event.getPlayer().getOpenInventory().getTopInventory()))
            destroy(this);
    }
}
