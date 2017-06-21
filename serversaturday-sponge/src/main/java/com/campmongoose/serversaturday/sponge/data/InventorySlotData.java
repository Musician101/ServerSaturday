package com.campmongoose.serversaturday.sponge.data;

import java.util.Optional;
import javax.annotation.Nonnull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractIntData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

public class InventorySlotData extends AbstractIntData<InventorySlotData, ImmutableInventorySlotData> {

    public InventorySlotData(int value) {
        super(value, SSKeys.INVENTORY_SLOT);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    protected Value<?> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue((Key<Value<Integer>>) usedKey, getValue());
    }

    @Nonnull
    @Override
    public Optional<InventorySlotData> fill(@Nonnull DataHolder dataHolder, @Nonnull MergeFunction overlap) {
        InventorySlotData merged = overlap.merge(this, dataHolder.get(InventorySlotData.class).orElse(null));
        setValue(merged.getValue());
        return Optional.of(this);
    }

    @Nonnull
    @Override
    public Optional<InventorySlotData> from(@Nonnull DataContainer container) {
        return container.getInt(usedKey.getQuery()).map(this::setValue);
    }

    @Nonnull
    @Override
    public InventorySlotData copy() {
        return new InventorySlotData(getValue());
    }

    @Nonnull
    @Override
    public ImmutableInventorySlotData asImmutable() {
        return new ImmutableInventorySlotData(getValue());
    }
}
