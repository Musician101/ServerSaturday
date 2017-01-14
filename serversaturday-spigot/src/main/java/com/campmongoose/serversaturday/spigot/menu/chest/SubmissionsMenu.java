package com.campmongoose.serversaturday.spigot.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotChestMenu;
import com.campmongoose.serversaturday.spigot.menu.anvil.page.JumpToPage;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmissions;
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

//TODO create paged menu
public class SubmissionsMenu extends AbstractSpigotChestMenu {

    private final int page;

    public SubmissionsMenu(@Nonnull Player player, int page, @Nullable AbstractSpigotChestMenu prevMenu) {
        super(Bukkit.createInventory(player, 54, MenuText.SUBMISSIONS), player, prevMenu);
        this.page = page;
    }

    @Override
    protected void build() {
        SpigotSubmissions submissions = SpigotServerSaturday.instance().getSubmissions();
        List<SpigotSubmitter> spigotSubmitters = submissions.getSubmitters();
        List<ItemStack> list = spigotSubmitters.stream().map(SpigotSubmitter::getMenuRepresentation).collect(Collectors.toList());
        for (int x = 0; x < 54; x++) {
            int subListPosition = x + (page - 1) * 45;
            if (x < 45 && list.size() > subListPosition) {
                ItemStack itemStack = list.get(subListPosition);
                set(x, itemStack, player -> {
                    for (SpigotSubmitter submitter : spigotSubmitters) {
                        if (submitter.getName().equals(itemStack.getItemMeta().getDisplayName())) {
                            new SubmitterMenu(player, submitter, 1, this);
                            return;
                        }
                    }
                });
            }
        }

        ItemStack jumpPage = new ItemStack(Material.BOOK);
        ItemMeta jumpPageMeta = jumpPage.getItemMeta();
        jumpPageMeta.setDisplayName(MenuText.JUMP_PAGE);
        jumpPage.setItemMeta(jumpPageMeta);
        set(45, jumpPage, player -> new JumpToPage(player, this, null));

        ItemStack prevPage = new ItemStack(Material.ARROW);
        ItemMeta prevPageMeta = prevPage.getItemMeta();
        prevPageMeta.setDisplayName(MenuText.PREVIOUS_PAGE);
        prevPage.setItemMeta(prevPageMeta);
        set(49, prevPage, player -> {
            if (page - 1 < 1) {
                new SubmissionsMenu(player, page - 1, prevMenu);
            }
        });

        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextPageMeta = nextPage.getItemMeta();
        nextPageMeta.setDisplayName(MenuText.NEXT_PAGE);
        set(51, nextPage, player -> {
            if (page + 1 > Integer.MAX_VALUE) {
                new SubmissionsMenu(player, page + 1, prevMenu);
            }
        });

        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MenuText.BACK);
        back.setItemMeta(backMeta);
        set(53, back, player -> {
            if (prevMenu == null) {
                close();
            }
            else {
                prevMenu.open();
            }
        });
    }
}
