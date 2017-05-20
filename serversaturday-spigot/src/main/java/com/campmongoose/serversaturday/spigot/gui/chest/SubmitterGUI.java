package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.spigot.gui.anvil.page.SubmitterJumpToPage;
import com.campmongoose.serversaturday.spigot.gui.chest.build.EditBuildGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.build.ViewBuildGUI;
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

public class SubmitterGUI extends AbstractSpigotPagedGUI {

    private final SpigotSubmitter submitter;

    public SubmitterGUI(@Nonnull Player player, @Nonnull SpigotSubmitter submitter, int page, @Nullable AbstractSpigotChestGUI prevMenu) {
        super(Bukkit.createInventory(player, 54, MenuText.submitterMenu(submitter)), player, page, prevMenu);
        this.submitter = submitter;
    }

    @Override
    protected void build() {
        List<ItemStack> list = submitter.getBuilds().stream().map(build -> build.getMenuRepresentation(submitter)).collect(Collectors.toList());
        setContents(list, (player, itemStack) -> p -> {
            for (SpigotBuild build : submitter.getBuilds()) {
                if (build.getName().equals(itemStack.getItemMeta().getDisplayName())) {
                    if (submitter.getUUID().equals(player.getUniqueId())) {
                        new EditBuildGUI(build, submitter, player, this);
                    }
                    else {
                        new ViewBuildGUI(build, submitter, player, this);
                    }

                    return;
                }
            }
        });

        int maxPage = new Double(Math.ceil(list.size() / 45)).intValue();
        ItemStack jumpStack = createItem(Material.BOOK, MenuText.JUMP_PAGE);
        jumpStack.setAmount(page);
        set(45, jumpStack, player -> new SubmitterJumpToPage(player, this, maxPage, submitter));
        setPageNavigationButton(48, MenuText.PREVIOUS_PAGE, player -> {
            if (page > 1) {
                new SubmitterGUI(player, submitter, page - 1, prevMenu);
            }
        });

        setPageNavigationButton(50, MenuText.NEXT_PAGE, player -> {
            if (page < maxPage) {
                new SubmitterGUI(player, submitter, page + 1, prevMenu);
            }
        });

        setBackButton(53, Material.BARRIER);
    }
}
