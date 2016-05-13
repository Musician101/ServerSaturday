package com.campmongoose.serversaturday.spigot.menu;

import com.campmongoose.serversaturday.common.menu.AbstractMenu;
import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotMenu.SpigotClickEventHandler;
import org.bukkit.Bukkit;
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
import java.util.UUID;

public abstract class AbstractSpigotMenu<M extends AbstractSpigotMenu> extends AbstractMenu<InventoryCloseEvent, Inventory, SpigotClickEventHandler, InventoryClickEvent, M, PlayerQuitEvent, ItemStack> implements Listener
{
    protected AbstractSpigotMenu(Inventory inv, SpigotClickEventHandler handler)
    {
        super(inv, handler);
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

    @Override
    protected void destroy(M menu)
    {
        HandlerList.unregisterAll(menu);
    }

    @Override
    public void open(UUID uuid)
    {
        Bukkit.getPlayer(uuid).openInventory(inv);
    }

    public static class SpigotSSClickEvent extends AbstractMenu.SSClickEvent<ItemStack, Player>
    {
        public SpigotSSClickEvent(Player player, ItemStack itemStack, int slot)
        {
            super(player, itemStack, slot);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public interface SpigotClickEventHandler extends AbstractMenu.ClickEventHandler<SpigotSSClickEvent, ItemStack, Player>
    {

    }
}
