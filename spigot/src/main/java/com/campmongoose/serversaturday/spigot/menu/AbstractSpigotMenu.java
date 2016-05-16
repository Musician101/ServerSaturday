package com.campmongoose.serversaturday.spigot.menu;

import com.campmongoose.serversaturday.common.menu.AbstractMenu;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotMenu.SpigotClickEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

public abstract class AbstractSpigotMenu extends AbstractMenu<InventoryCloseEvent, Inventory, SpigotClickEventHandler, InventoryClickEvent, PlayerQuitEvent, ItemStack> implements Listener
{
    protected AbstractSpigotMenu(Inventory inv, SpigotClickEventHandler handler)
    {
        super(inv, handler);
        SpigotServerSaturday.getInstance().getServer().getPluginManager().registerEvents(this, SpigotServerSaturday.getInstance());
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
            destroy();
    }

    @EventHandler
    @Override
    public void onClose(InventoryCloseEvent event)
    {
        if (!(event.getPlayer() instanceof Player))
            return;

        Inventory inv = event.getInventory();
        if (inv.equals(this.inv))
            destroy();
    }

    @EventHandler
    @Override
    public void onQuit(PlayerQuitEvent event)
    {
        if (inv.equals(event.getPlayer().getOpenInventory().getTopInventory()))
            destroy();
    }

    @Override
    protected void destroy()
    {
        HandlerList.unregisterAll(this);
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
