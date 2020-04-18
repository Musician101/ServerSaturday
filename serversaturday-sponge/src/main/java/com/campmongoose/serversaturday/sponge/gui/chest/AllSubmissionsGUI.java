package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.gui.SpongeIconBuilder;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;

public class AllSubmissionsGUI extends SpongeChestGUI {

    private int page = 1;

    public AllSubmissionsGUI(@Nonnull Player player) {
        super(player, Text.of(MenuText.ALL_SUBMISSIONS), 54);
        updateSlots();
        setButton(50, SpongeIconBuilder.of(ItemTypes.BARRIER, Text.of("Back")), ImmutableMap.of(ClickInventoryEvent.Primary.class, Player::closeInventory));
    }

    private void updateSlots() {
        Multimap<SpongeBuild, SpongeSubmitter> map = TreeMultimap.create(Comparator.comparing(SpongeBuild::getName), Comparator.comparing(SpongeSubmitter::getName));
        SpongeServerSaturday.instance().getSubmissions().getSubmitters().forEach(submitter -> submitter.getBuilds().stream().filter(build -> !build.featured() && build.submitted()).forEach(build -> map.put(build, submitter)));
        List<SpongeBuild> builds = new ArrayList<>(map.keys());
        IntStream.of(0, 45).forEach(x -> {
            try {
                int index = x + (page - 1) * 45;
                SpongeBuild build = builds.get(index);
                SpongeSubmitter submitter = map.get(build).iterator().next();
                map.remove(build, submitter);
                setButton(x, build.getMenuRepresentation(submitter), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> {
                    if (p.getUniqueId().equals(submitter.getUUID())) {
                        new EditBuildGUI(build, submitter, p);
                        return;
                    }

                    new ViewBuildGUI(build, submitter, p);
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
                updateSlots();
            }));
        }

        int maxPage = Double.valueOf(Math.ceil(map.size() / 45d)).intValue();
        if (page < maxPage) {
            removeButton(53);
        }
        else {
            setButton(53, SpongeIconBuilder.of(ItemTypes.ARROW, Text.of(MenuText.NEXT_PAGE)), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> {
                page++;
                updateSlots();
            }));
        }
    }
}
