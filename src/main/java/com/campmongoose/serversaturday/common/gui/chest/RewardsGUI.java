package com.campmongoose.serversaturday.common.gui.chest;

import javax.annotation.Nonnull;

public abstract class RewardsGUI<I, P> {

    protected final I inventory;
    protected final P player;

    protected RewardsGUI(@Nonnull I inventory, @Nonnull P player) {
        this.inventory = inventory;
        this.player = player;
    }
}
