package com.campmongoose.serversaturday.sponge.data.manipulator.mutable;

import com.campmongoose.serversaturday.sponge.data.key.SSKeys;
import com.campmongoose.serversaturday.sponge.data.manipulator.immutable.ImmutableInventorySlotData;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

public class InventorySlotData extends AbstractSingleData<Integer, InventorySlotData, ImmutableInventorySlotData> {

    public InventorySlotData(Integer value) {
        super(value, SSKeys.SLOT);
    }

    @Nonnull
    @Override
    public ImmutableInventorySlotData asImmutable() {
        return new ImmutableInventorySlotData(getValue());
    }

    @Nonnull
    @Override
    public InventorySlotData copy() {
        return new InventorySlotData(getValue());
    }

    @Nonnull
    @Override
    public Optional<InventorySlotData> fill(@Nonnull DataHolder dataHolder, @Nonnull MergeFunction overlap) {
        dataHolder.get(InventorySlotData.class).ifPresent(inventorySlotData -> setValue(inventorySlotData.getValue()));
        return Optional.of(this);
    }

    @Nonnull
    @Override
    public Optional<InventorySlotData> from(@Nonnull DataContainer container) {
        container.getInt(SSKeys.SLOT.getQuery()).ifPresent(this::setValue);
        return Optional.of(this);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    protected Value<Integer> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue((Key<Value<Integer>>) usedKey, getValue());
    }

    @Nonnull
    @Override
    protected DataContainer fillContainer(@Nonnull DataContainer dataContainer) {
        return dataContainer.set(usedKey, getValue());
    }
}
