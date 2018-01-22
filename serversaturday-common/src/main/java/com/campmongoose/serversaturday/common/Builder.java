package com.campmongoose.serversaturday.common;

import javax.annotation.Nonnull;

public interface Builder<B extends Builder, T> {

    @Nonnull
    T build();

    @Nonnull
    B reset();
}
