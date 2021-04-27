package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.google.common.collect.ImmutableMap;
import io.musician101.musicianlibrary.java.minecraft.sponge.gui.chest.SpongeIconBuilder;
import javax.annotation.Nonnull;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.menu.ClickTypes;

public class ViewBuildGUI extends BuildGUI {

    public ViewBuildGUI(@Nonnull Build<Component> build, @Nonnull Submitter<Component> submitter, @Nonnull ServerPlayer player) {
        super(build, 3, 0, player);
        setButton(1, SpongeIconBuilder.builder(ItemTypes.BOOK).name(Component.text(MenuText.DESCRIPTION_NAME)).description(Component.text(MenuText.DESCRIPTION_DESC)).build(), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> p.openBook(Book.book(Component.text(build.getName()), Component.text(submitter.getName()), build.getDescription()))));
        setButton(2, SpongeIconBuilder.builder(ItemTypes.PAINTING).name(Component.text(MenuText.RESOURCE_PACK_NAME)).description(Component.text(MenuText.RESOURCE_PACK_DESC)).build(), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> p.openBook(Book.book(Component.text(build.getName()), Component.text(submitter.getName()), build.getResourcePacks()))));
        setButton(8, SpongeIconBuilder.of(ItemTypes.BARRIER, Component.text("Back")), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> new SubmitterGUI(submitter, p)));
    }
}
