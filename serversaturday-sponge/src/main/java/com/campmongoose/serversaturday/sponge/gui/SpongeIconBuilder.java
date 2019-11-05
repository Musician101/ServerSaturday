package com.campmongoose.serversaturday.sponge.gui;

import com.campmongoose.serversaturday.common.gui.AbstractIconBuilder;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
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
    public SpongeIconBuilder addGlow(boolean addGlow) {
        if (addGlow) {
            if (itemStack.getType() == ItemTypes.ENCHANTED_BOOK) {
                itemStack.offer(Keys.STORED_ENCHANTMENTS, Collections.singletonList(Enchantment.of(EnchantmentTypes.UNBREAKING, 1)));
            }
            else {
                itemStack.offer(Keys.ITEM_ENCHANTMENTS, Collections.singletonList(Enchantment.of(EnchantmentTypes.UNBREAKING, 1)));
            }

            itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
        }

        return this;
    }

    @Nonnull
    @Override
    public SpongeIconBuilder amount(int amount) {
        itemStack.setQuantity(amount);
        return this;
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
    public SpongeIconBuilder potionEffect(@Nonnull PotionEffectType potionType) {
        itemStack.offer(Keys.POTION_EFFECTS, Collections.singletonList(PotionEffect.of(potionType, 1, 1)));
        return this;
    }

    @Nonnull
    @Override
    public SpongeIconBuilder reset() {
        itemStack = ItemStack.of(itemStack.getType(), 1);
        return this;
    }

    @Nonnull
    public <T extends CatalogType> ItemStack type(ItemStack itemStack, Key<Value<T>> key, T type) {
        itemStack.offer(key, type);
        return itemStack;
    }
}
