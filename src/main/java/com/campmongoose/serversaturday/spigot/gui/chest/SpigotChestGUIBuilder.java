package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.gui.chest.ChestGUIBuilder;
import com.campmongoose.serversaturday.common.gui.chest.GUIButton;
import com.campmongoose.serversaturday.spigot.gui.SpigotIconBuilder;
import com.campmongoose.serversaturday.spigot.gui.anvil.SpigotJumpToPage;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class SpigotChestGUIBuilder extends ChestGUIBuilder<SpigotChestGUIBuilder, ClickType, SpigotChestGUI, Inventory, Player, ItemStack, String> {

    SpigotChestGUIBuilder() {

    }

    @Nonnull
    @Override
    public SpigotChestGUI build() {
        checkNotNull(name, "Inventory name cannot be null.");
        checkArgument(page > 0, "Page must be greater than 0");
        checkNotNull(player, "Player cannot be null.");
        checkArgument(size > 0 && size % 9 == 0, "Size must be greater than 0 and be a multiple of 9.");
        return new SpigotChestGUI(player, name, size, buttons, page, manualOpen);
    }

    @Nonnull
    @Override
    public SpigotChestGUIBuilder setJumpToPage(int slot, int maxPage, @Nonnull Consumer<Integer> action) {
        return setButton(new GUIButton<>(slot, ClickType.LEFT, SpigotIconBuilder.builder(Material.BOOK).name("Jump To Page").amount(page).build(), () -> new SpigotJumpToPage(player, maxPage, action)));
    }

    @Nonnull
    @Override
    public SpigotChestGUIBuilder setPageNavigation(int slot, @Nonnull String name, @Nonnull Runnable action) {
        return setButton(new GUIButton<>(slot, ClickType.LEFT, SpigotIconBuilder.of(Material.ARROW, name), action));
    }
}
