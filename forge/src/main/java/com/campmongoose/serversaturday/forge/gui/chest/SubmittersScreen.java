package com.campmongoose.serversaturday.forge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.forge.ForgeServerSaturday;
import com.campmongoose.serversaturday.forge.gui.ForgeIconBuilder;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import com.google.common.collect.ImmutableMap;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import org.bukkit.event.inventory.ClickType;
//TODO replace with Forge GUIs
@Deprecated
public class SubmittersScreen extends SSForgeScreen {

    private int page = 1;

    public SubmittersScreen(@Nonnull ServerPlayerEntity player) {
        super(MenuText.SUBMISSIONS);
        updateSlots();
        setButton(50, ForgeIconBuilder.of(Items.BARRIER, "Back"), ImmutableMap.of(ClickType.LEFT, SubmittersScreen::new));
    }

    private void updateSlots() {
        List<ForgeSubmitter> submitters = ForgeServerSaturday.getInstance().getSubmissions().getSubmitters();
        submitters.sort(Comparator.comparing(ForgeSubmitter::getName));
        IntStream.of(0, 45).forEach(x -> {
            try {
                int index = x + (page - 1) * 45;
                ForgeSubmitter submitter = submitters.get(index);
                setButton(x, submitter.getMenuRepresentation(), ImmutableMap.of(ClickType.LEFT, p -> new SubmitterScreen(submitter, p)));
            }
            catch (IndexOutOfBoundsException ignored) {

            }
        });

        if (page == 1) {
            removeButton(45);
        }
        else {
            setButton(45, ForgeIconBuilder.of(Items.ARROW, MenuText.PREVIOUS_PAGE), ImmutableMap.of(ClickType.LEFT, p -> {
                page--;
                updateSlots();
            }));
        }

        int maxPage = Double.valueOf(Math.ceil(submitters.size() / 45d)).intValue();
        if (page < maxPage) {
            removeButton(53);
        }
        else {
            setButton(53, ForgeIconBuilder.of(Items.ARROW, MenuText.NEXT_PAGE), ImmutableMap.of(ClickType.LEFT, p -> {
                page++;
                updateSlots();
            }));
        }
    }
}
