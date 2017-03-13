package com.campmongoose.serversaturday.spigot.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotChestMenu;
import com.campmongoose.serversaturday.spigot.menu.anvil.page.SubmitterJumpToPage;
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

public class SubmitterMenu extends SpigotAbstractPagedMenu {

    private final SpigotSubmitter submitter;

    public SubmitterMenu(@Nonnull Player player, @Nonnull SpigotSubmitter submitter, int page, @Nullable AbstractSpigotChestMenu prevMenu) {
        super(Bukkit.createInventory(player, 54, MenuText.submitterMenu(submitter)), player, page, prevMenu);
        this.submitter = submitter;
    }

    @Override
    protected void build() {
        List<ItemStack> list = submitter.getBuilds().stream().map(build -> build.getMenuRepresentation(submitter)).collect(Collectors.toList());
        for (int x = 0; x < 54; x++) {
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

        int maxPage = new Double(Math.ceil(list.size() / 45)).intValue();
        ItemStack jumpStack = createItem(Material.BOOK, MenuText.JUMP_PAGE);
        jumpStack.setAmount(page);
        set(45, jumpStack, player -> new SubmitterJumpToPage(player, this, maxPage, submitter));
        setPageNavigationButton(48, MenuText.PREVIOUS_PAGE, player -> {
            if (page > 1) {
                new SubmitterMenu(player, submitter, page - 1, prevMenu);
            }
        });

        setPageNavigationButton(50, MenuText.NEXT_PAGE, player -> {
            if (page < maxPage) {
                new SubmitterMenu(player, submitter, page + 1, prevMenu);
            }
        });

        setBackButton(53, Material.BARRIER);
    }
}
