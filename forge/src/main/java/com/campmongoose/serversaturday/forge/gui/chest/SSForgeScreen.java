package com.campmongoose.serversaturday.forge.gui.chest;

import com.campmongoose.serversaturday.common.gui.chest.ChestGUI;
import com.campmongoose.serversaturday.forge.ForgeServerSaturday;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

//TODO replace with Forge GUIs
@Deprecated
public abstract class SSForgeScreen extends Screen {

    protected SSForgeScreen(@Nonnull String name) {
        super(new StringTextComponent(name));
    }

    @Override
    protected void addItem(int slot, @Nonnull ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }

    @Override
    protected boolean isCorrectInventory(@Nonnull InventoryView view) {
        return view.getTitle().equals(name) && view.getPlayer().getUniqueID().equals(player.getUniqueID());
    }

    @EventHandler
    public final void onInventoryClick(InventoryClickEvent event) {
        if (isCorrectInventory(event.getView())) {
            if (buttons.stream().anyMatch(button -> button.getSlot() == event.getRawSlot())) {
                event.setCancelled(true);
                buttons.stream().filter(button -> button.getSlot() == event.getRawSlot()).findFirst().ifPresent(button -> button.handle(event.getClick(), (ServerPlayerEntity) event.getWhoClicked()));
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
        Bukkit.getScheduler().scheduleSyncDelayedTask(ForgeServerSaturday.getInstance(), () -> {
            inventory.clear();
            buttons.forEach(button -> inventory.setItem(button.getSlot(), button.getItemStack()));
            player.openInventory(inventory);
            Bukkit.getPluginManager().registerEvents(this, ForgeServerSaturday.getInstance());
        });
    }

    @Override
    protected void removeItem(int slot) {
        inventory.setItem(slot, null);
    }
}
