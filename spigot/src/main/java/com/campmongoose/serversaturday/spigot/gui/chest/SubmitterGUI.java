package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.google.common.collect.ImmutableMap;
import io.musician101.musicianlibrary.java.minecraft.spigot.gui.chest.SpigotIconBuilder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SubmitterGUI extends SpigotServerSaturdayChestGUI {

    private int page = 1;

    public SubmitterGUI(@Nonnull Submitter<String> submitter, @Nonnull Player player) {
        super(player, MenuText.submitterMenu(submitter), 54);
        updateSlots(submitter);
        if (player.getUniqueId().equals(submitter.getUUID())) {
            setButton(51, SpigotIconBuilder.of(Material.EMERALD_BLOCK, MenuText.NEW_BUILD), ImmutableMap.of(ClickType.LEFT, p -> {

            }));
        }
        setButton(50, SpigotIconBuilder.of(Material.BARRIER, "Back"), ImmutableMap.of(ClickType.LEFT, SubmittersGUI::new));
    }

    private void updateSlots(@Nonnull Submitter<String> submitter) {
        List<Build<String>> builds = submitter.getBuilds();
        builds.sort(Comparator.comparing(Build::getName));
        IntStream.of(0, 45).forEach(x -> {
            try {
                int index = x + (page - 1) * 45;
                Build<String> build = builds.get(index);
                ItemStack itemStack = SpigotIconBuilder.builder(Material.BOOK).name((build.submitted() && !build.featured() ? ChatColor.GREEN : ChatColor.RED) + submitter.getName()).description(Collections.singletonList("By " + submitter.getName())).build();
                setButton(x, itemStack, ImmutableMap.of(ClickType.LEFT, p -> {
                    if (submitter.getUUID().equals(p.getUniqueId())) {
                        new EditBuildGUI(build, submitter, p);
                        return;
                    }

                    new ViewBuildGUI(build, submitter, p);
                }, ClickType.RIGHT, p -> {
                    if (submitter.getUUID().equals(p.getUniqueId())) {
                        builds.remove(index);
                        updateSlots(submitter);
                    }
                }));
            }
            catch (IndexOutOfBoundsException ignored) {

            }
        });

        if (page == 1) {
            removeButton(45);
        }
        else {
            setButton(45, SpigotIconBuilder.of(Material.ARROW, MenuText.PREVIOUS_PAGE), ImmutableMap.of(ClickType.LEFT, p -> {
                page--;
                updateSlots(submitter);
            }));
        }

        int maxPage = Double.valueOf(Math.ceil(builds.size() / 45d)).intValue();
        if (page < maxPage) {
            removeButton(53);
        }
        else {
            setButton(53, SpigotIconBuilder.of(Material.ARROW, MenuText.NEXT_PAGE), ImmutableMap.of(ClickType.LEFT, p -> {
                page++;
                updateSlots(submitter);
            }));
        }
    }
}
