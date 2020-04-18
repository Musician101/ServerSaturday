package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.gui.chest.ChestGUI;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public abstract class SpigotChestGUI extends ChestGUI<ClickType, Inventory, Player, ItemStack, String, InventoryView> implements Listener {

    protected SpigotChestGUI(@Nonnull Player player, @Nonnull String name, int size) {
        super(parseInventory(player, name, size), name, player);
    }

    private static Inventory parseInventory(@Nonnull Player player, @Nonnull String name, int size) {
        return Bukkit.createInventory(player, size, name);
    }

    @Override
    protected void addItem(int slot, @Nonnull ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }

    @Override
    protected boolean isCorrectInventory(@Nonnull InventoryView view) {
        return view.getTitle().equals(name) && view.getPlayer().getUniqueId().equals(player.getUniqueId());
    }

    @EventHandler
    public final void onInventoryClick(InventoryClickEvent event) {
        if (isCorrectInventory(event.getView())) {
            if (buttons.stream().anyMatch(button -> button.getSlot() == event.getRawSlot())) {
                event.setCancelled(true);
                buttons.stream().filter(button -> button.getSlot() == event.getRawSlot()).findFirst().ifPresent(button -> button.handle(event.getClick(), (Player) event.getWhoClicked()));
            }
        }
    }

    @EventHandler
    public final void onInventoryClose(InventoryCloseEvent event) {
        if (isCorrectInventory(event.getView())) {
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler
    public final void onInventoryItemDrag(InventoryDragEvent event) {
        if (isCorrectInventory(event.getView())) {
            if (buttons.stream().anyMatch(button -> event.getRawSlots().contains(button.getSlot()))) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    protected final void open() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotServerSaturday.instance(), () -> {
            inventory.clear();
            buttons.forEach(button -> inventory.setItem(button.getSlot(), button.getItemStack()));
            player.openInventory(inventory);
            Bukkit.getPluginManager().registerEvents(this, SpigotServerSaturday.instance());
        });
    }

    @Override
    protected void removeItem(int slot) {
        inventory.setItem(slot, null);
    }
}
