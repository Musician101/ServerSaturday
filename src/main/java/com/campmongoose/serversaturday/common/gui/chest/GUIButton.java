package com.campmongoose.serversaturday.common.gui.chest;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class GUIButton<C, S> {

    @Nullable
    private final Runnable action;
    @Nonnull
    private final C clickType;
    @Nonnull
    private final S itemStack;
    private final int slot;

    public GUIButton(int slot, @Nonnull C clickType, @Nonnull S itemStack, @Nullable Runnable action) {
        this.slot = slot;
        this.clickType = clickType;
        this.itemStack = itemStack;
        this.action = action;
    }

    @Nonnull
    public Optional<Runnable> getAction() {
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
