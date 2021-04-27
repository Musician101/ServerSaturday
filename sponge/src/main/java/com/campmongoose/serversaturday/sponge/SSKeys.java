package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.Reference;
import java.util.UUID;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.util.TypeTokens;

public final class SSKeys {

    public static final Key<Value<UUID>> UUID = Key.of(ResourceKey.of(Reference.ID, "uuid"), TypeTokens.UUID_VALUE_TOKEN);

    private SSKeys() {

    }
}
