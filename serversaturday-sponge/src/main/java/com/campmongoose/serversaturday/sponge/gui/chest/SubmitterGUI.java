package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.sponge.gui.SpongeIconBuilder;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import com.google.common.collect.ImmutableMap;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;

public class SubmitterGUI extends SpongeChestGUI {

    private int page = 1;

    public SubmitterGUI(@Nonnull SpongeSubmitter submitter, @Nonnull Player player) {
        super(player, Text.of(MenuText.submitterMenu(submitter)), 54);
        updateSlots(submitter);
        if (player.getUniqueId().equals(submitter.getUUID())) {
            setButton(51, SpongeIconBuilder.of(ItemTypes.EMERALD_BLOCK, Text.of(MenuText.NEW_BUILD)), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> {

            }));
        }
        setButton(50, SpongeIconBuilder.of(ItemTypes.BARRIER, Text.of("Back")), ImmutableMap.of(ClickInventoryEvent.Primary.class, SubmittersGUI::new));
    }

    private void updateSlots(@Nonnull SpongeSubmitter submitter) {
        List<SpongeBuild> builds = submitter.getBuilds();
        builds.sort(Comparator.comparing(SpongeBuild::getName));
        IntStream.of(0, 45).forEach(x -> {
            try {
                int index = x + (page - 1) * 45;
                SpongeBuild build = builds.get(index);
                setButton(x, build.getMenuRepresentation(submitter), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> {
                    if (submitter.getUUID().equals(p.getUniqueId())) {
                        new EditBuildGUI(build, submitter, p);
                        return;
                    }

                    new ViewBuildGUI(build, submitter, p);
                }, ClickInventoryEvent.Secondary.class, p -> {
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
            setButton(45, SpongeIconBuilder.of(ItemTypes.ARROW, Text.of(MenuText.PREVIOUS_PAGE)), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> {
                page--;
                updateSlots(submitter);
            }));
        }

        int maxPage = Double.valueOf(Math.ceil(builds.size() / 45d)).intValue();
        if (page < maxPage) {
            removeButton(53);
        }
        else {
            setButton(53, SpongeIconBuilder.of(ItemTypes.ARROW, Text.of(MenuText.NEXT_PAGE)), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> {
                page++;
                updateSlots(submitter);
            }));
        }
    }
}
