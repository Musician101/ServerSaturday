package com.campmongoose.serversaturday.sponge.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.sponge.menu.textinput.JumpToPage;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

public abstract class AbstractSpongePagedGUI extends AbstractSpongeChestGUI {

    protected final int page;

    public AbstractSpongePagedGUI(@Nonnull String name, int size, @Nonnull Player player, int page, @Nullable AbstractSpongeChestGUI prevMenu) {
        super(name, size, player, prevMenu);
        this.page = page;
    }

    protected void setContents(List<ItemStack> contents, BiFunction<Player, ItemStack, Consumer<Player>> consumerMapper) {
        for (int x = 0; x < 45; x++) {
            try {
                ItemStack itemStack = contents.get(x + (page - 1) * 45);
                if (consumerMapper != null) {
                    set(x, itemStack, consumerMapper.apply(player, itemStack));
                }
                else {
                    set(x, itemStack);
                }
            }
            catch (IndexOutOfBoundsException e) {
                //Used to skip populating slots if the list is too small to fill the page.
            }
        }
    }

    protected void setJumpToPage(int slot, int maxPage) {
        ItemStack itemStack = createItem(ItemTypes.BOOK, Text.of(MenuText.JUMP_PAGE));
        itemStack.setQuantity(page);
        set(slot, itemStack, player -> new JumpToPage(player, prevMenu, maxPage));
    }

    protected void setPageNavigationButton(int slot, @Nonnull String name, @Nonnull Consumer<Player> consumer) {
        set(slot, createItem(ItemTypes.ARROW, Text.of(name)), consumer);
    }
}
