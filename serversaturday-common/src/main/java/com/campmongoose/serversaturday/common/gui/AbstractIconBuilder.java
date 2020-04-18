package com.campmongoose.serversaturday.common.gui;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;

public abstract class AbstractIconBuilder<B extends AbstractIconBuilder<B, I, P, T>, I, P, T> {

    @Nonnull
    protected I itemStack;

    protected AbstractIconBuilder(@Nonnull I itemStack) {
        this.itemStack = itemStack;
    }

    @Nonnull
    public final I build() {
        return itemStack;
    }

    @SafeVarargs
    @Nonnull
    public final B description(@Nonnull T... description) {
        return description(Arrays.asList(description));
    }

    @Nonnull
    public abstract B description(@Nonnull List<T> description);

    @Nonnull
    public abstract B name(@Nonnull T name);

    @Nonnull
    public abstract B reset();
}
