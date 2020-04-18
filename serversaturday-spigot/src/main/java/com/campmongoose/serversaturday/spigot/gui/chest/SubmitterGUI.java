package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.spigot.gui.SpigotIconBuilder;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import com.google.common.collect.ImmutableMap;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class SubmitterGUI extends SpigotChestGUI {

    private int page = 1;

    public SubmitterGUI(@Nonnull SpigotSubmitter submitter, @Nonnull Player player) {
        super(player, MenuText.submitterMenu(submitter), 54);
        updateSlots(submitter);
        if (player.getUniqueId().equals(submitter.getUUID())) {
            setButton(51, SpigotIconBuilder.of(Material.EMERALD_BLOCK, MenuText.NEW_BUILD), ImmutableMap.of(ClickType.LEFT, p -> {

            }));
        }
        setButton(50, SpigotIconBuilder.of(Material.BARRIER, "Back"), ImmutableMap.of(ClickType.LEFT, SubmittersGUI::new));
    }

    private void updateSlots(@Nonnull SpigotSubmitter submitter) {
        List<SpigotBuild> builds = submitter.getBuilds();
        builds.sort(Comparator.comparing(SpigotBuild::getName));
        IntStream.of(0, 45).forEach(x -> {
            try {
                int index = x + (page - 1) * 45;
                SpigotBuild build = builds.get(index);
                setButton(x, build.getMenuRepresentation(submitter), ImmutableMap.of(ClickType.LEFT, p -> {
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
