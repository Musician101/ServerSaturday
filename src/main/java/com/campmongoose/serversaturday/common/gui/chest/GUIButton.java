package com.campmongoose.serversaturday.common.gui.chest;

import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class GUIButton<C, P, S> {

    @Nullable
    private final Consumer<P> action;
    @Nonnull
    private final C clickType;
    @Nonnull
    private final S itemStack;
    private final int slot;

    public GUIButton(int slot, @Nonnull C clickType, @Nonnull S itemStack, @Nullable Consumer<P> action) {
        this.slot = slot;
        this.clickType = clickType;
        this.itemStack = itemStack;
        this.action = action;
    }

    @Nonnull
    public Optional<Consumer<P>> getAction() {
        return Optional.ofNullable(action);
    }

    @Nonnull
    public final C getClickType() {
        return clickType;
    }

    @Nonnull
    public final S getItemStack() {
        return itemStack;
    }

    public final int getSlot() {
        return slot;
    }
}
