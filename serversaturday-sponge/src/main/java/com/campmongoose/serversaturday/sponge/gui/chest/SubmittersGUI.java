package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.gui.SpongeIconBuilder;
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

public class SubmittersGUI extends SpongeChestGUI {

    private int page = 1;

    public SubmittersGUI(@Nonnull Player player) {
        super(player, Text.of(MenuText.SUBMISSIONS), 54);
        updateSlots();
        setButton(50, SpongeIconBuilder.of(ItemTypes.BARRIER, Text.of("Back")), ImmutableMap.of(ClickInventoryEvent.Primary.class, SubmittersGUI::new));
    }

    private void updateSlots() {
        List<SpongeSubmitter> submitters = SpongeServerSaturday.instance().getSubmissions().getSubmitters();
        submitters.sort(Comparator.comparing(SpongeSubmitter::getName));
        IntStream.of(0, 45).forEach(x -> {
            try {
                int index = x + (page - 1) * 45;
                SpongeSubmitter submitter = submitters.get(index);
                setButton(x, submitter.getMenuRepresentation(), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> new SubmitterGUI(submitter, p)));
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

        int maxPage = Double.valueOf(Math.ceil(submitters.size() / 45d)).intValue();
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
