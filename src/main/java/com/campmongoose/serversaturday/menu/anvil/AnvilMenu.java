package com.campmongoose.serversaturday.menu.anvil;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.menu.AbstractMenu;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.ContainerAnvil;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilMenu extends AbstractMenu {

    AnvilMenu(ClickHandler handler) {
        super(null, handler);
        Bukkit.getServer().getPluginManager().registerEvents(this, ServerSaturday.instance());
    }

    @Override
    protected <M extends AbstractMenu> void destroy(M menu) {
        HandlerList.unregisterAll(menu);
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

        if (!inv.equals(event.getInventory())) {
            return;
        }

        if (event.getSlotType() != SlotType.RESULT) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if (!isSameInventory(event.getInventory(), player)) {
            return;
        }

        event.setCancelled(true);
        float exp = player.getExp();
        int level = player.getLevel();
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) {
            return;
        }

        if (!itemStack.hasItemMeta()) {
            return;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!itemMeta.hasDisplayName()) {
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

        player.setExp(exp);
        player.setLevel(level);
    }

    @EventHandler
    @Override
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        Inventory inv = event.getInventory();
        if (isSameInventory(inv, (Player) event.getPlayer())) {
            inv.clear();
            destroy(this);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        event.setCancelled(isSameInventory(event.getInventory(), (Player) event.getWhoClicked()));
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

    class AnvilContainer extends ContainerAnvil {

        AnvilContainer(EntityPlayer player) {
            super(player.inventory, player.world, new BlockPosition(0, 0, 0), player);
        }

        @Override
        public boolean canUse(EntityHuman entityHuman) {
            return true;
        }
    }
}
