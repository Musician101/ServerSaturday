package com.campmongoose.serversaturday.common;

import java.util.List;
import javax.annotation.Nonnull;

public interface SQLTextSerializer<T> {

    @Nonnull
    List<T> deserialize(@Nonnull String string);

    String serialize(@Nonnull List<T> list);
}
