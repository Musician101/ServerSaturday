package com.campmongoose.serversaturday.forge.gui;

import com.campmongoose.serversaturday.common.gui.AbstractIconBuilder;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;
import org.bukkit.potion.PotionType;

//TODO not needed for Forge GUIs
@Deprecated
public final class ForgeIconBuilder extends AbstractIconBuilder<ForgeIconBuilder, ItemStack, PotionType, String> {

    private ForgeIconBuilder(Material material) {
        super(new ItemStack(material));
    }

    public static ForgeIconBuilder builder(@Nonnull Material material) {
        return new ForgeIconBuilder(material);
    }

    public static ItemStack of(@Nonnull Material material, @Nonnull String name) {
        return builder(material).name(name).build();
    }

    @Nonnull
    @Override
    public ForgeIconBuilder description(@Nonnull List<String> description) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(description);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @Nonnull
    @Override
    public ForgeIconBuilder name(@Nonnull String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @Nonnull
    @Override
    public ForgeIconBuilder reset() {
        itemStack = new ItemStack(itemStack.getItem());
        return this;
    }
}
