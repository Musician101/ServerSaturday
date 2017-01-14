package com.campmongoose.serversaturday.spigot.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotChestMenu;
import com.campmongoose.serversaturday.spigot.menu.anvil.page.JumpToPage;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SubmitterMenu extends AbstractSpigotChestMenu {

    private final int page;
    private final SpigotSubmitter submitter;

    public SubmitterMenu(@Nonnull Player player, @Nonnull SpigotSubmitter submitter, int page, @Nullable AbstractSpigotChestMenu prevMenu)
    {
        super(Bukkit.createInventory(player, 54, MenuText.submitterMenu(submitter)), player, prevMenu);
        this.submitter = submitter;
        this.page = page;
    }

    @Override
    protected void build() {
        List<ItemStack> list = submitter.getBuilds().stream().map(build -> build.getMenuRepresentation(submitter)).collect(Collectors.toList());
        for (int x = 0; x < 54; x++)
        {
            int subListPosition = x + (page - 1) * 45;
            if (x < 45 && list.size() > subListPosition) {
                ItemStack itemStack = list.get(subListPosition);
                set(x, itemStack, player -> {
                    for (SpigotBuild build : submitter.getBuilds()) {
                        if (build.getName().equals(itemStack.getItemMeta().getDisplayName())) {
                            new BuildMenu(build, submitter, player, this);
                        }
                    }
                });
            }
        }
        
        //TODO need back button in all menus
        ItemStack jumpPage = new ItemStack(Material.BOOK);
        ItemMeta jumpPageMeta = jumpPage.getItemMeta();
        jumpPageMeta.setDisplayName(MenuText.JUMP_PAGE);
        jumpPage.setItemMeta(jumpPageMeta);
        set(45, jumpPage, player -> new JumpToPage(player, this, submitter));

        ItemStack prevPage = new ItemStack(Material.ARROW);
        ItemMeta prevPageMeta = prevPage.getItemMeta();
        prevPageMeta.setDisplayName(MenuText.PREVIOUS_PAGE);
        prevPage.setItemMeta(prevPageMeta);
        set(49, prevPage, player -> {
            if (page - 1 < 1) {
                new SubmitterMenu(player, submitter, page - 1, prevMenu);
            }
        });

        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextPageMeta = nextPage.getItemMeta();
        nextPageMeta.setDisplayName(MenuText.NEXT_PAGE);
        set(51, nextPage, player -> {
            if (page + 1 > Integer.MAX_VALUE) {
                new SubmitterMenu(player, submitter, page + 1, prevMenu);
            }
        });

        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MenuText.BACK);
        back.setItemMeta(backMeta);
        set(53, back, player -> {
            if (prevMenu == null)
                close();
            else
                prevMenu.open();
        });
    }
}
