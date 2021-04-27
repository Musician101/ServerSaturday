package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.google.common.collect.ImmutableMap;
import io.musician101.musicianlibrary.java.minecraft.spigot.gui.book.SpigotBookGUI;
import io.musician101.musicianlibrary.java.minecraft.spigot.gui.chest.SpigotIconBuilder;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class ViewBuildGUI extends BuildGUI {

    public ViewBuildGUI(@Nonnull Build<String> build, @Nonnull Submitter<String> submitter, @Nonnull Player player) {
        super(build, 3, 0, player);
        setButton(1, SpigotIconBuilder.builder(Material.BOOK).name(MenuText.DESCRIPTION_NAME).description(MenuText.DESCRIPTION_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> handleOpenBook(p, build.getDescription(), build, submitter)));
        setButton(2, SpigotIconBuilder.builder(Material.PAINTING).name(MenuText.RESOURCE_PACK_NAME).description(MenuText.RESOURCE_PACK_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> handleOpenBook(p, build.getResourcePacks(), build, submitter)));
        setButton(8, SpigotIconBuilder.of(Material.BARRIER, "Back"), ImmutableMap.of(ClickType.LEFT, p -> new SubmitterGUI(submitter, p)));
    }

    private void handleOpenBook(Player player, List<String> pages, Build<String> build, Submitter<String> submitter) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setAuthor(submitter.getName());
        bookMeta.setPages(pages);
        bookMeta.setTitle(build.getName());
        book.setItemMeta(bookMeta);
        SpigotBookGUI.open(player, book);
    }
}
