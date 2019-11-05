package com.campmongoose.serversaturday.sponge.data.manipulator.immutable;

import com.campmongoose.serversaturday.sponge.data.key.SSKeys;
import com.campmongoose.serversaturday.sponge.data.manipulator.mutable.InventorySlotData;
import javax.annotation.Nonnull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

public class ImmutableInventorySlotData extends AbstractImmutableSingleData<Integer, ImmutableInventorySlotData, InventorySlotData> {

    public ImmutableInventorySlotData(Integer value) {
        super(value, SSKeys.SLOT);
    }

    @Nonnull
    @Override
    public InventorySlotData asMutable() {
        return new InventorySlotData(getValue());
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    protected ImmutableValue<Integer> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue((Key<Value<Integer>>) usedKey, getValue()).asImmutable();
    }

    @Nonnull
    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(SSKeys.SLOT, getValue());
    }
}
