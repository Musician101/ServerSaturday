package com.campmongoose.serversaturday.menu.chest;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.menu.AbstractMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class ChestMenu extends AbstractMenu {

    ChestMenu(Inventory inv, ClickHandler handler) {
        super(inv, handler);
        Bukkit.getServer().getPluginManager().registerEvents(this, ServerSaturday.instance());
    }

    @Override
    protected <M extends AbstractMenu> void destroy(M menu) {
        HandlerList.unregisterAll(menu);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        event.setCancelled(isSameInventory(event.getInventory(), (Player) event.getWhoClicked()));
    }

    @EventHandler
    @Override
    public void onClick(InventoryClickEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if (!isSameInventory(event.getInventory(), player)) {
            return;
        }

        event.setCancelled(true);
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }

        int slot = event.getRawSlot();
        SSClickEvent clickEvent = new SSClickEvent(player, itemStack, slot);
        handler.handle(clickEvent);
        if (clickEvent.willClose()) {
            player.closeInventory();
        }

        if (clickEvent.willDestroy()) {
            destroy(this);
        }
    }

    @EventHandler
    @Override
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        if (isSameInventory(event.getInventory(), (Player) event.getPlayer())) {
            destroy(this);
        }
    }

    @EventHandler
    @Override
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        InventoryView inv = player.getOpenInventory();
        if (inv.getType() == InventoryType.CRAFTING) {
            return;
        }

        if (isSameInventory(player.getOpenInventory().getTopInventory(), player)) {
            destroy(this);
        }
    }
}
