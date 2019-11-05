package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.gui.chest.ChestGUIBuilder;
import com.campmongoose.serversaturday.common.gui.chest.GUIButton;
import com.campmongoose.serversaturday.sponge.gui.SpongeIconBuilder;
import com.campmongoose.serversaturday.sponge.gui.anvil.SpongeJumpToPage;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class SpongeChestGUIBuilder extends ChestGUIBuilder<SpongeChestGUIBuilder, Class<? extends ClickInventoryEvent>, SpongeChestGUI, Inventory, Player, ItemStack, Text> {

    SpongeChestGUIBuilder() {

    }

    @Nonnull
    @Override
    public SpongeChestGUI build() {
        checkNotNull(name, "Inventory name cannot be null.");
        checkArgument(page > 0, "Page must be greater than 0");
        checkNotNull(player, "Player cannot be null.");
        checkArgument(size > 0 && size % 9 == 0, "Size must be greater than 0 and be a multiple of 9.");
        return new SpongeChestGUI(player, name, size, buttons, page, manualOpen);
    }

    @Nonnull
    @Override
    public SpongeChestGUIBuilder setJumpToPage(int slot, int maxPage, @Nonnull BiConsumer<Player, Integer> action) {
        return setButton(new GUIButton<>(slot, ClickInventoryEvent.Primary.class, SpongeIconBuilder.builder(ItemTypes.BOOK).name(Text.of("Jump To Page")).amount(page).build(), p -> new SpongeJumpToPage(p, maxPage, action)));
    }

    @Nonnull
    @Override
    public SpongeChestGUIBuilder setPageNavigation(int slot, @Nonnull Text name, @Nonnull Consumer<Player> action) {
        return setButton(new GUIButton<>(slot, ClickInventoryEvent.Primary.class, SpongeIconBuilder.of(ItemTypes.ARROW, name), action));
    }
}
