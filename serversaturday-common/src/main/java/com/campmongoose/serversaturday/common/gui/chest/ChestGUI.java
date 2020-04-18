package com.campmongoose.serversaturday.common.gui.chest;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nonnull;

public abstract class ChestGUI<ClickType, Inventory, Player, ItemStack, Text, InventoryView> {

    @Nonnull
    protected final List<GUIButton<ClickType, Player, ItemStack>> buttons = new ArrayList<>();
    @Nonnull
    protected final Inventory inventory;
    @Nonnull
    protected final Text name;
    @Nonnull
    protected final Player player;

    protected ChestGUI(@Nonnull Inventory inventory, @Nonnull Text name, @Nonnull Player player) {
        this.inventory = inventory;
        this.name = name;
        this.player = player;
        open();
    }

    protected abstract void addItem(int slot, @Nonnull ItemStack itemStack);

    protected abstract boolean isCorrectInventory(@Nonnull InventoryView view);

    protected abstract void open();

    public final void removeButton(int slot) {
        buttons.removeIf(g -> g.getSlot() == slot);
        removeItem(slot);
    }

    protected abstract void removeItem(int slot);

    public final void setButton(int slot, @Nonnull ItemStack itemStack, Map<ClickType, Consumer<Player>> actions) {
        buttons.removeIf(g -> g.getSlot() == slot);
        buttons.add(new GUIButton<>(slot, itemStack, actions));
        addItem(slot, itemStack);
    }

    public final void setButton(int slot, @Nonnull ItemStack itemStack) {
        setButton(slot, itemStack, ImmutableMap.of());
    }
}
