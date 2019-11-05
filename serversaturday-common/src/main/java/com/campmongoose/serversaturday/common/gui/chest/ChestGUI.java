package com.campmongoose.serversaturday.common.gui.chest;

import java.util.List;
import javax.annotation.Nonnull;

public abstract class ChestGUI<C, I, P, S, T> {

    @Nonnull
    protected final List<GUIButton<C, P, S>> buttons;
    @Nonnull
    protected final I inventory;
    @Nonnull
    protected final T name;
    protected final int page;
    @Nonnull
    protected final P player;

    protected ChestGUI(@Nonnull I inventory, @Nonnull T name, @Nonnull P player, @Nonnull List<GUIButton<C, P, S>> buttons, int page, boolean manualOpen) {
        this.inventory = inventory;
        this.name = name;
        this.player = player;
        this.buttons = buttons;
        this.page = page;
        if (!manualOpen) {
            open();
        }
    }

    public abstract void close();

    @Nonnull
    public T getName() {
        return name;
    }

    public abstract void open();
}
