package com.campmongoose.serversaturday.sponge.data.manipulator.builder;

import com.campmongoose.serversaturday.sponge.data.key.SSKeys;
import com.campmongoose.serversaturday.sponge.data.manipulator.immutable.ImmutableUUIDData;
import com.campmongoose.serversaturday.sponge.data.manipulator.mutable.UUIDData;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

public class UUIDDataBuilder implements DataManipulatorBuilder<UUIDData, ImmutableUUIDData> {

    private UUID uuid;

    @Nonnull
    @Override
    public Optional<UUIDData> build(@Nonnull DataView container) throws InvalidDataException {
        Optional<UUID> uuidOptional = container.getString(SSKeys.UUID.getQuery()).map(UUID::fromString);
        if (!uuidOptional.isPresent()) {
            throw new InvalidDataException("UUID needs to be set.");
        }

        setUuid(uuidOptional.get());
        return Optional.of(create());
    }

    @Nonnull
    @Override
    public UUIDData create() {
        if (uuid == null) {
            throw new InvalidDataException("UUID needs to be set.");
        }

        return new UUIDData(uuid);
    }

    @Nonnull
    @Override
    public Optional<UUIDData> createFrom(@Nonnull DataHolder dataHolder) {
        Optional<UUID> uuidOptional = dataHolder.get(SSKeys.UUID);
        if (!uuidOptional.isPresent()) {
            throw new InvalidDataException("UUID needs to be set.");
        }

        setUuid(uuidOptional.get());
        return Optional.of(create());
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
