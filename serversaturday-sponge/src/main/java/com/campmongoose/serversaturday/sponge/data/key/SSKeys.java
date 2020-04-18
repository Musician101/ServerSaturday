package com.campmongoose.serversaturday.sponge.data.key;

import java.util.UUID;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.TypeTokens;

public final class SSKeys {

    @SuppressWarnings("UnstableApiUsage")
    public static final Key<Value<UUID>> UUID = Key.builder().type(TypeTokens.UUID_VALUE_TOKEN).query(DataQuery.of("UUID")).id("uuid").name("UUID").build();

    private SSKeys() {

    }
}
