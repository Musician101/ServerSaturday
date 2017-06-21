package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.submission.SubmissionsNotLoadedException;
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

public class SubmissionsGUI extends AbstractSpigotPagedGUI {

    private final int page;

    public SubmissionsGUI(@Nonnull Player player, int page, @Nullable AbstractSpigotChestGUI prevMenu) {
        super(Bukkit.createInventory(player, 54, MenuText.SUBMISSIONS), player, page, prevMenu);
        this.page = page;
    }

    @Override
    protected void build() {
        SpigotSubmissions submissions;
        try {
            submissions = SpigotServerSaturday.instance().getSubmissions();
        }
        catch (SubmissionsNotLoadedException e) {
            player.sendMessage(ChatColor.RED + e.getMessage());
            return;
        }
        List<SpigotSubmitter> spigotSubmitters = submissions.getSubmitters();
        List<ItemStack> list = spigotSubmitters.stream().map(SpigotSubmitter::getMenuRepresentation).collect(Collectors.toList());
        setContents(spigotSubmitters, SpigotSubmitter::getMenuRepresentation, (player, submitter) -> p -> new SubmitterGUI(p, submitter, 1, this));

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

        setBackButton(53, Material.BARRIER);
    }
}
