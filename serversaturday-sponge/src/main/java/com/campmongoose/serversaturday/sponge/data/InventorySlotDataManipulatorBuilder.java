package com.campmongoose.serversaturday.sponge.data;

import java.util.Optional;
import javax.annotation.Nonnull;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

public class InventorySlotDataManipulatorBuilder implements DataManipulatorBuilder<InventorySlotData, ImmutableInventorySlotData> {

    private int slot = 0;

    public InventorySlotDataManipulatorBuilder setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    @Nonnull
    @Override
    public Optional<InventorySlotData> build(@Nonnull DataView container) throws InvalidDataException {
        return container.getInt(SSKeys.INVENTORY_SLOT.getQuery()).map(InventorySlotData::new);
    }

    @Nonnull
    @Override
    public DataBuilder<InventorySlotData> reset() {
        this.slot = 0;
        return this;
    }

    @Nonnull
    @Override
    public InventorySlotData create() {
        return new InventorySlotData(slot);
    }

    @Nonnull
    @Override
    public Optional<InventorySlotData> createFrom(@Nonnull DataHolder dataHolder) {
        return dataHolder.get(SSKeys.INVENTORY_SLOT).map(InventorySlotData::new);
    }
}
