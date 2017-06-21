package com.campmongoose.serversaturday.sponge.data;

import javax.annotation.Nonnull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

public class ImmutableInventorySlotData extends AbstractImmutableSingleData<Integer, ImmutableInventorySlotData, InventorySlotData> {

    public ImmutableInventorySlotData(Integer value) {
        super(value, SSKeys.INVENTORY_SLOT);
    }

    public int slot() {
        return getValue();
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    protected ImmutableValue<?> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue((Key<Value<Integer>>) usedKey, getValue()).asImmutable();
    }

    @Nonnull
    @Override
    public InventorySlotData asMutable() {
        return new InventorySlotData(getValue());
    }

    @Nonnull
    @Override
    public DataContainer toContainer() {
        DataContainer dataContainer = super.toContainer();
        dataContainer.set(usedKey, getValue());
        return dataContainer;
    }
}
