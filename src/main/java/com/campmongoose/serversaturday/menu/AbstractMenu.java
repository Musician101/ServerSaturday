package com.campmongoose.serversaturday.menu;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class AbstractMenu<Menu extends AbstractMenu> implements Listener
{
    protected final ClickHandler handler;
    protected Inventory inv;

    protected AbstractMenu(Inventory inv, ClickHandler handler)
    {
        this.inv = inv;
        this.handler = handler;
    }

    protected void setOption(int slot, ItemStack itemStack)
    {
        setOption(slot, itemStack, " ");
    }

    protected void setOption(int slot, ItemStack itemStack, String name)
    {
        setOption(slot, itemStack, name, new String[0]);
    }

    protected void setOption(int slot, ItemStack itemStack, String name, String... description)
    {
        setOption(slot, itemStack, name, false, description);
    }

    protected void setOption(int slot, ItemStack itemStack, String name, boolean willGlow, String... description)
    {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(description));
        if (willGlow)
        {
            itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        itemStack.setItemMeta(itemMeta);
        inv.setItem(slot, itemStack);
    }

    public abstract void onClick(InventoryClickEvent event);

    public abstract void onClose(InventoryCloseEvent event);

    public abstract void onQuit(PlayerQuitEvent event);

    protected <M extends AbstractMenu> void destroy(M menu)
    {
        HandlerList.unregisterAll(menu);
    }

    public void open(Player player)
    {
        player.openInventory(inv);
    }

    public interface ClickHandler
    {
        void handle(SSClickEvent event);
    }

    public static class SSClickEvent
    {
        boolean close = true;
        boolean destroy = true;
        final int slot;
        final ItemStack itemStack;
        final Player player;

        public SSClickEvent(Player player, ItemStack itemStack, int slot)
        {
            this.player = player;
            this.itemStack = itemStack;
            this.slot = slot;
        }

        public boolean willClose()
        {
            return close;
        }

        public void setWillClose(@SuppressWarnings("SameParameterValue") boolean close)
        {
            this.close = close;
        }

        public boolean willDestroy()
        {
            return destroy;
        }

        public void setWillDestroy(boolean destroy)
        {
            this.destroy = destroy;
        }

        public int getSlot()
        {
            return slot;
        }

        public ItemStack getItem()
        {
            return itemStack;
        }

        public Player getPlayer()
        {
            return player;
        }
    }
}
