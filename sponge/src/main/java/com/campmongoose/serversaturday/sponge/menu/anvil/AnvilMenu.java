package com.campmongoose.serversaturday.sponge.menu.anvil;

import com.campmongoose.serversaturday.sponge.menu.AbstractSpongeMenu;
import org.spongepowered.api.item.inventory.custom.CustomInventory;

@SuppressWarnings("WeakerAccess")
public class AnvilMenu extends AbstractSpongeMenu
{
    AnvilMenu(SpongeClickEventHandler handler)
    {
        super(CustomInventory.builder().size(3).build(), handler);
    }
}
