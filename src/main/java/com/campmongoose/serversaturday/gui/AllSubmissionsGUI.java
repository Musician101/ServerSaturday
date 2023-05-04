package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.Reference.MenuText;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import static io.musician101.musigui.spigot.chest.SpigotIconUtil.customName;
import static io.musician101.musigui.spigot.chest.SpigotIconUtil.setLore;

public class AllSubmissionsGUI extends ServerSaturdayChestGUI {

    private int page = 1;

    public AllSubmissionsGUI(@Nonnull Player player) {
        super(player, MenuText.ALL_SUBMISSIONS, 54);
        updateSlots();
        setButton(50, customName(new ItemStack(Material.BARRIER), "Back"), Map.of(ClickType.LEFT, Player::closeInventory));
    }

    private void updateSlots() {
        Multimap<Build, Submitter> map = TreeMultimap.create(Comparator.comparing(Build::getName), Comparator.comparing(Submitter::getName));
        ServerSaturday.getInstance().getSubmissions().getSubmitters().forEach(submitter -> submitter.getBuilds().stream().filter(build -> !build.featured() && build.submitted()).forEach(build -> map.put(build, submitter)));
        List<Build> builds = new ArrayList<>(map.keys());
        IntStream.range(0, 45).forEach(x -> {
            try {
                int index = x + (page - 1) * 45;
                Build build = builds.get(index);
                Submitter submitter = map.get(build).iterator().next();
                map.remove(build, submitter);
                String name = (build.submitted() && !build.featured() ? ChatColor.GREEN : ChatColor.RED) + build.getName();
                ItemStack itemStack = setLore(customName(new ItemStack(Material.BOOK), name), "By " + submitter.getName());
                setButton(x, itemStack, ClickType.LEFT, p -> {
                    if (p.getUniqueId().equals(submitter.getUUID())) {
                        new EditBuildGUI(build, submitter, p);
                        return;
                    }

                    new ViewBuildGUI(build, submitter, p);
                });
            }
            catch (IndexOutOfBoundsException ignored) {

            }
        });

        if (page == 1) {
            removeButton(45);
        }
        else {
            setButton(45, customName(new ItemStack(Material.ARROW), MenuText.PREVIOUS_PAGE), ClickType.LEFT, p -> {
                page--;
                updateSlots();
            });
        }

        int maxPage = Double.valueOf(Math.ceil(map.size() / 45d)).intValue();
        if (page >= maxPage) {
            removeButton(53);
        }
        else {
            setButton(53, customName(new ItemStack(Material.ARROW), MenuText.NEXT_PAGE), ClickType.LEFT, p -> {
                page++;
                updateSlots();
            });
        }
    }
}
