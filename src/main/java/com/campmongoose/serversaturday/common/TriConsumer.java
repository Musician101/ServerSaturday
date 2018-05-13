package com.campmongoose.serversaturday.common;

import javax.annotation.Nullable;

@FunctionalInterface
public interface TriConsumer<T, U, V> {

    void accept(@Nullable T t, @Nullable U u, @Nullable V v);
}
