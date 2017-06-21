package com.campmongoose.serversaturday.sponge.data;

import com.campmongoose.serversaturday.common.Reference;
import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.value.mutable.Value;

public class SSKeys {

    public static final Key<Value<Integer>> INVENTORY_SLOT = KeyFactory.makeSingleKey(TypeToken.of(Integer.class), new TypeToken<Value<Integer>>() {}, DataQuery.of("InventorySlot"), Reference.ID + "inventory_slot", "InventorySlot");

    private SSKeys() {

    }
}
