package com.campmongoose.serversaturday.spigot.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.spigot.menu.anvil.page.JumpToPage;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class SpigotAbstractPagedMenu extends AbstractSpigotChestMenu {

    protected final int page;

    public SpigotAbstractPagedMenu(@Nonnull Inventory inventory, @Nonnull Player player, int page, @Nullable AbstractSpigotChestMenu prevMenu) {
        super(inventory, player, prevMenu);
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
        ItemStack itemStack = createItem(Material.BOOK, MenuText.JUMP_PAGE);
        itemStack.setAmount(page);
        set(slot, itemStack, player -> new JumpToPage(player, prevMenu, maxPage));
    }

    protected void setPageNavigationButton(int slot, @Nonnull String name, @Nonnull Consumer<Player> consumer) {
        set(slot, createItem(Material.ARROW, name), consumer);
    }
}
