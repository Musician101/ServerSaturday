package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmissions;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SubmissionsGUI extends SpigotAbstractPagedGUI {

    private final int page;

    public SubmissionsGUI(@Nonnull Player player, int page, @Nullable AbstractSpigotChestGUI prevMenu) {
        super(Bukkit.createInventory(player, 54, MenuText.SUBMISSIONS), player, page, prevMenu);
        this.page = page;
    }

    @Override
    protected void build() {
        SpigotSubmissions submissions = SpigotServerSaturday.instance().getSubmissions();
        List<SpigotSubmitter> spigotSubmitters = submissions.getSubmitters();
        List<ItemStack> list = spigotSubmitters.stream().map(SpigotSubmitter::getMenuRepresentation).collect(Collectors.toList());
        setContents(list, (player, itemStack) -> p -> {
            for (SpigotSubmitter submitter : spigotSubmitters) {
                if (submitter.getName().equals(itemStack.getItemMeta().getDisplayName())) {
                    new SubmitterGUI(p, submitter, 1, this);
                    return;
                }
            }
        });

        int maxPage = new Double(Math.ceil(list.size() / 45)).intValue();
        setJumpToPage(45, maxPage);
        setPageNavigationButton(48, MenuText.PREVIOUS_PAGE, player -> {
            if (page > 1) {
                new SubmissionsGUI(player, page - 1, prevMenu);
            }
        });

        setPageNavigationButton(50, MenuText.NEXT_PAGE, player -> {
            if (page + 1 > Integer.MAX_VALUE) {
                new SubmissionsGUI(player, page + 1, prevMenu);
            }
        });

        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.RED + MenuText.BACK);
        back.setItemMeta(backMeta);
        set(53, back, player -> {
            if (prevMenu == null) {
                player.closeInventory();
            }
            else {
                prevMenu.open();
            }
        });
    }
}
