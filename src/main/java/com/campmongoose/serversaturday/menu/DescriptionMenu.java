package com.campmongoose.serversaturday.menu;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Collections;

class DescriptionMenu
{
    private final ItemStack book = new ItemStack(Material.BOOK_AND_QUILL)
    {
        {
            BookMeta bookMeta = (BookMeta) getItemMeta();
            bookMeta.setPages(Collections.singletonList("Replace me with a description of you build!"));
            setItemMeta(bookMeta);
        }
    };

    public void open(Player player)
    {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        ep.openBook(CraftItemStack.asNMSCopy(book));
    }
}
