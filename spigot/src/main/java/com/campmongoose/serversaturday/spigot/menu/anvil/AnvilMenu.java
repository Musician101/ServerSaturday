package com.campmongoose.serversaturday.spigot.menu.anvil;

import com.campmongoose.serversaturday.spigot.ServerSaturday;
import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotMenu;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.ContainerAnvil;
import net.minecraft.server.v1_9_R1.EntityHuman;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("WeakerAccess")
public class AnvilMenu extends AbstractSpigotMenu<AnvilMenu>
{
    AnvilMenu(SpigotClickEventHandler handler)
    {
        super(null, handler);
        ServerSaturday.getInstance().getServer().getPluginManager().registerEvents(this, ServerSaturday.getInstance());
    }

    @EventHandler
    @Override
    public void onClick(InventoryClickEvent event)//NOSONAR
    {
        if (!(event.getWhoClicked() instanceof Player))
            return;

        if (!inv.equals(event.getInventory()))
            return;

        if (event.getSlotType() != SlotType.RESULT)
            return;

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        float exp = player.getExp();
        int level = player.getLevel();
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null)
            return;

        if (!itemStack.hasItemMeta())
            return;

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!itemMeta.hasDisplayName())
            return;

        int slot = event.getRawSlot();
        SpigotSSClickEvent clickEvent = new SpigotSSClickEvent(player, itemStack, slot);
        handler.handle(clickEvent);
        if (clickEvent.willClose())
            player.closeInventory();

        if (clickEvent.willDestroy())
            destroy(this);

        player.setExp(exp);
        player.setLevel(level);
    }

    @EventHandler
    @Override
    public void onClose(InventoryCloseEvent event)
    {
        if (!(event.getPlayer() instanceof Player))
            return;

        Inventory inv = event.getInventory();
        if (inv.equals(this.inv))
        {
            inv.clear();
            destroy(this);
        }
    }

    @EventHandler
    @Override
    public void onQuit(PlayerQuitEvent event)
    {
        if (inv.equals(event.getPlayer().getOpenInventory().getTopInventory()))
            destroy(this);
    }

    class AnvilContainer extends ContainerAnvil
    {
        AnvilContainer(EntityPlayer player)
        {
            super(player.inventory, player.world, new BlockPosition(0, 0, 0), player);
        }

        @Override
        public boolean a(EntityHuman entityHuman)
        {
            return true;
        }
    }
}
