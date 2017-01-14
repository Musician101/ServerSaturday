package com.campmongoose.serversaturday.common.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractChestMenu<I, M extends AbstractChestMenu<I, M, P, S>, P, S> {

    protected final Map<Integer, Consumer<P>> buttons = new HashMap<>();
    @Nonnull
    protected final I inventory;
    @Nonnull
    protected final P player;
    @Nullable
    protected final M prevMenu;

    public AbstractChestMenu(@Nonnull I inventory, @Nonnull P player, @Nullable M prevMenu, boolean manualOpen) {
        this.inventory = inventory;
        this.player = player;
        this.prevMenu = prevMenu;
        if (!manualOpen) {
            open();
        }
    }

    protected abstract void build();

    protected abstract void close();

    public abstract void open();

    protected abstract void set(int slot, @Nonnull S itemStack);

    protected abstract void set(int slot, @Nonnull S itemStack, @Nonnull Consumer<P> consumer);
}
