package com.campmongoose.serversaturday.sponge.menu.chest;

import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.menu.AbstractSpongeMenu;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.inventory.type.OrderedInventory;


@SuppressWarnings("WeakerAccess")
public class ChestMenu extends AbstractSpongeMenu
{
    ChestMenu(OrderedInventory inv, SpongeClickEventHandler handler)
    {
        super(inv, handler);
        Sponge.getEventManager().registerListeners(SpongeServerSaturday.instance(), this);
    }
}
