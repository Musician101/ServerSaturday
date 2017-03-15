package com.campmongoose.serversaturday.spigot.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmissions;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AllSubmissionsMenu extends SpigotAbstractPagedMenu {

    public AllSubmissionsMenu(@Nonnull Player player, int page, @Nullable AbstractSpigotChestMenu prevMenu) {
        super(Bukkit.createInventory(null, 54, MenuText.ALL_SUBMISSIONS), player, page, prevMenu);
    }

    @Override
    protected void build() {
        List<ItemStack> list = new ArrayList<>();
        SpigotServerSaturday.instance().getSubmissions().getSubmitters().forEach(submitter ->
                list.addAll(submitter.getBuilds().stream().map(build ->
                        build.getMenuRepresentation(submitter)).collect(Collectors.toList())));

        setContents(list, (p, itemStack) ->
                player -> {
                    String submitterName = itemStack.getItemMeta().getLore().get(0);
                    SpigotServerSaturday plugin = SpigotServerSaturday.instance();
                    SpigotSubmitter submitter = null;
                    SpigotSubmissions submissions = plugin.getSubmissions();
                    UUID uuid = plugin.getUUIDCache().getUUIDOf(submitterName);
                    if (uuid != null) {
                        submitter = submissions.getSubmitter(uuid);
                    }
                    else {
                        for (SpigotSubmitter s : submissions.getSubmitters()) {
                            if (submitterName.equals(s.getName())) {
                                submitter = s;
                            }
                        }
                    }

                    if (submitter == null) {
                        player.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_FOUND);
                        return;
                    }

                    new SubmitterMenu(player, submitter, 1, this);
                });

        int maxPage = new Double(Math.ceil(list.size() / 45)).intValue();
        setJumpToPage(45, maxPage);
        setPageNavigationButton(48, MenuText.PREVIOUS_PAGE, player -> {
            if (page > 1) {
                new AllSubmissionsMenu(player, page - 1, this);
            }
        });

        setPageNavigationButton(50, MenuText.NEXT_PAGE, player -> {
            if (page < maxPage) {
                new AllSubmissionsMenu(player, page + 1, this);
            }
        });

        setBackButton(53, Material.BARRIER);
    }
}
