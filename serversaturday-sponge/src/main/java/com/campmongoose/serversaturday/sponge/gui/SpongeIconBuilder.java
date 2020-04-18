package com.campmongoose.serversaturday.sponge.gui;

import com.campmongoose.serversaturday.common.gui.AbstractIconBuilder;
import java.util.List;
import javax.annotation.Nonnull;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

public class SpongeIconBuilder extends AbstractIconBuilder<SpongeIconBuilder, ItemStack, PotionEffectType, Text> {

    private SpongeIconBuilder(ItemType itemType) {
        super(ItemStack.of(itemType, 1));
    }

    public static SpongeIconBuilder builder(ItemType itemType) {
        return new SpongeIconBuilder(itemType);
    }

    public static ItemStack of(ItemType itemType, Text name) {
        return builder(itemType).name(name).build();
    }

    @Nonnull
    @Override
    public SpongeIconBuilder description(@Nonnull List<Text> description) {
        itemStack.offer(Keys.ITEM_LORE, description);
        return this;
    }

    @Nonnull
    @Override
    public SpongeIconBuilder name(@Nonnull Text name) {
        itemStack.offer(Keys.DISPLAY_NAME, name);
        return this;
    }

    @Nonnull
    @Override
    public SpongeIconBuilder reset() {
        itemStack = ItemStack.of(itemStack.getType(), 1);
        return this;
    }
}
