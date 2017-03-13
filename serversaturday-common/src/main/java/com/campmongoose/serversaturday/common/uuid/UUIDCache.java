package com.campmongoose.serversaturday.common.uuid;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.io.IOException;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UUIDCache {

    private final BiMap<UUID, String> uuidMap = HashBiMap.create();

    public UUIDCache() {

    }

    public void add(@Nonnull UUID uuid, @Nonnull String name) {
        uuidMap.put(uuid, name);
    }

    public void addOffline(@Nonnull UUID uuid) throws IOException {
        uuidMap.put(uuid, UUIDUtils.getNameOf(uuid));
    }

    @Nullable
    public String getNameOf(@Nonnull UUID uuid) {
        return uuidMap.get(uuid);
    }

    @Nullable
    public UUID getUUIDOf(@Nonnull String name) {
        return uuidMap.inverse().get(name);
    }
}
