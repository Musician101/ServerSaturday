package com.campmongoose.serversaturday.common.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractChestMenu<F, I, M extends AbstractChestMenu<F, I, M, P, S, T>, P, S, T> {

    protected final Map<Integer, Consumer<P>> buttons = new HashMap<>();
    @Nonnull
    protected final I inventory;
    @Nonnull
    protected final P player;
    @Nullable
    protected final M prevMenu;

    public AbstractChestMenu(@Nonnull I inventory, @Nonnull P player, @Nullable M prevMenu) {
        this.inventory = inventory;
        this.player = player;
        this.prevMenu = prevMenu;
    }

    protected abstract void build();

    protected abstract void close();

    public abstract void open();

    protected abstract void set(int slot, @Nonnull S itemStack);

    protected abstract void set(int slot, @Nonnull S itemStack, @Nonnull Consumer<P> consumer);

    protected abstract void setBackButton(int slot, @Nonnull T itemType);

    @Nonnull
    protected abstract S createItem(@Nonnull T itemType, @Nonnull F name, @Nonnull F... description);
}