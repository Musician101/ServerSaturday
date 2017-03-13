package com.campmongoose.serversaturday.common;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface TriConsumer<T, U, V> {

    void accept(@Nonnull T t, @Nonnull U u, @Nonnull V v);
}
