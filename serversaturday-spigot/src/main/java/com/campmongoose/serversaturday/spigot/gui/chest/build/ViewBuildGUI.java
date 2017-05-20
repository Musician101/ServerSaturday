package com.campmongoose.serversaturday.spigot.gui.chest.build;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.spigot.gui.chest.AbstractSpigotChestGUI;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.server.v1_11_R1.EnumHand;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class ViewBuildGUI extends BuildGUI {

    public ViewBuildGUI(@Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter, @Nonnull Player player, @Nullable AbstractSpigotChestGUI prevMenu) {
        super(build, submitter, player, prevMenu, true);
        if (!submitter.getUUID().equals(player.getUniqueId())) {
            open();
        }
    }

    @Override
    protected void build() {
        setTeleportButton(0, build.getLocation());
        set(1, createItem(Material.BOOK, MenuText.DESCRIPTION_NAME),
                player -> {
                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                    BookMeta bookMeta = (BookMeta) book.getItemMeta();
                    bookMeta.setAuthor(submitter.getName());
                    bookMeta.setPages(build.getDescription());
                    bookMeta.setTitle(build.getName());
                    book.setItemMeta(bookMeta);
                    ItemStack old = player.getInventory().getItemInMainHand();
                    player.getInventory().setItemInMainHand(book);
                    ((CraftPlayer) player).getHandle().a(CraftItemStack.asNMSCopy(book), EnumHand.MAIN_HAND);
                    player.getInventory().setItemInMainHand(old);
                });
        set(2, createItem(Material.PAINTING, MenuText.RESOURCE_PACK_NAME, build.getResourcePack().toArray(new String[0])));
        setFeatureButton(3);
        setBackButton(8, Material.ARROW);
    }
}
