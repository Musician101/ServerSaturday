package com.campmongoose.serversaturday.common.gui.chest;

import java.util.List;
import javax.annotation.Nonnull;

public abstract class ChestGUI<C, G extends ChestGUI<C, G, I, P, S>, I, P, S> {

    protected final List<GUIButton<C, G, P, S>> buttons;
    protected final I inventory;
    protected final int page;
    protected final P player;
    protected final G prevGUI;

    protected ChestGUI(I inventory, P player, List<GUIButton<C, G, P, S>> buttons, int page, G prevGUI, boolean manualOpen) {
        this.inventory = inventory;
        this.player = player;
        this.buttons = buttons;
        this.page = page;
        this.prevGUI = prevGUI;
        if (!manualOpen) {
            open();
        }
    }

    public abstract void close();

    @Nonnull
    public abstract String getName();

    public abstract void open();
}