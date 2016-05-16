package com.campmongoose.serversaturday.spigot.menu.chest;

import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotMenu;
import org.bukkit.inventory.Inventory;

@SuppressWarnings("WeakerAccess")
public class ChestMenu extends AbstractSpigotMenu
{
    ChestMenu(Inventory inv, SpigotClickEventHandler handler)
    {
        super(inv, handler);
    }
}
