package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.gui.chest.build.EditBuildGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.build.ViewBuildGUI;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmissions;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AllSubmissionsGUI extends AbstractSpigotPagedGUI {

    public AllSubmissionsGUI(@Nonnull Player player, int page, @Nullable AbstractSpigotChestGUI prevMenu) {
        super(Bukkit.createInventory(null, 54, MenuText.ALL_SUBMISSIONS), player, page, prevMenu);
    }

    @Override
    protected void build() {
        SpigotSubmissions submissions;
        try {
            submissions = SpigotServerSaturday.instance().getSubmissions();
        }
        catch (SubmissionsNotLoadedException e) {
            player.closeInventory();
            player.sendMessage(ChatColor.RED + e.getMessage());
            return;
        }

        List<SpigotSubmitter> submitters = submissions.getSubmitters();
        Multimap<SpigotBuild, SpigotSubmitter> map = HashMultimap.create();
        submitters.forEach(submitter -> submitter.getBuilds().forEach(build -> map.put(build, submitter)));
        setContents(new ArrayList<>(map.keySet()), build -> {
            SpigotSubmitter submitter = new ArrayList<>(map.get(build)).get(0);
            return build.getMenuRepresentation(submitter);
        }, (p, build) -> {
            SpigotSubmitter submitter = new ArrayList<>(map.get(build)).get(0);
            map.remove(build, submitter);
            return player -> {
                if (player.getUniqueId().equals(this.player.getUniqueId())) {
                    new EditBuildGUI(build, submitter, player, this);
                }
                else {
                    new ViewBuildGUI(build, submitter, player, this);
                }
            };
        });

        int maxPage = new Double(Math.ceil(map.keySet().size() / 45)).intValue();
        setJumpToPage(45, maxPage);
        setPageNavigationButton(48, MenuText.PREVIOUS_PAGE, player -> {
            if (page > 1) {
                new AllSubmissionsGUI(player, page - 1, this);
            }
        });

        setPageNavigationButton(50, MenuText.NEXT_PAGE, player -> {
            if (page < maxPage) {
                new AllSubmissionsGUI(player, page + 1, this);
            }
        });

        setBackButton(53, Material.BARRIER);
    }
}
