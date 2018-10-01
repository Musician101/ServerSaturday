package com.campmongoose.serversaturday.common.gui.chest;

import java.util.List;
import javax.annotation.Nonnull;

public abstract class ChestGUI<C, I, P, S> {

    protected final List<GUIButton<C, P, S>> buttons;
    protected final I inventory;
    protected final int page;
    protected final P player;

    protected ChestGUI(I inventory, P player, List<GUIButton<C, P, S>> buttons, int page, boolean manualOpen) {
        this.inventory = inventory;
        this.player = player;
        this.buttons = buttons;
        this.page = page;
        if (!manualOpen) {
            open();
        }
    }

    public abstract void close();

    @Nonnull
    public abstract String getName();

    public abstract void open();
}
