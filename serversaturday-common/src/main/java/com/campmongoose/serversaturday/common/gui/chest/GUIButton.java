package com.campmongoose.serversaturday.common.gui.chest;

import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nonnull;

public final class GUIButton<C, P, S> {

    @Nonnull
    private final Map<C, Consumer<P>> actions;
    @Nonnull
    private final S itemStack;
    private final int slot;

    public GUIButton(int slot, @Nonnull S itemStack, @Nonnull Map<C, Consumer<P>> actions) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.actions = actions;
    }

    @Nonnull
    public final S getItemStack() {
        return itemStack;
    }

    public final int getSlot() {
        return slot;
    }

    public void handle(@Nonnull C clickType, @Nonnull P player) {
        actions.getOrDefault(clickType, p -> {
        }).accept(player);
    }
}
