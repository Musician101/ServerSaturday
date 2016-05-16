package com.campmongoose.serversaturday.spigot.menu.anvil;

import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotMenu;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.ContainerAnvil;
import net.minecraft.server.v1_9_R1.EntityHuman;
import net.minecraft.server.v1_9_R1.EntityPlayer;

@SuppressWarnings("WeakerAccess")
public class AnvilMenu extends AbstractSpigotMenu
{
    AnvilMenu(SpigotClickEventHandler handler)
    {
        super(null, handler);
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
