package com.campmongoose.serversaturday.spigot.gui;

import com.campmongoose.serversaturday.common.gui.AbstractIconBuilder;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;

public final class SpigotIconBuilder extends AbstractIconBuilder<SpigotIconBuilder, ItemStack, PotionType, String> {

    private SpigotIconBuilder(Material material) {
        super(new ItemStack(material));
    }

    public static SpigotIconBuilder builder(@Nonnull Material material) {
        return new SpigotIconBuilder(material);
    }

    public static ItemStack of(@Nonnull Material material, @Nonnull String name) {
        return builder(material).name(name).build();
    }

    @Nonnull
    @Override
    public SpigotIconBuilder description(@Nonnull List<String> description) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(description);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @Nonnull
    @Override
    public SpigotIconBuilder name(@Nonnull String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @Nonnull
    @Override
    public SpigotIconBuilder reset() {
        itemStack = new ItemStack(itemStack.getType());
        return this;
    }
}
