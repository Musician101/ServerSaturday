package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.List;
import java.util.stream.Collectors;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

public class SubmissionsGUI extends AbstractSpongePagedGUI {

    public SubmissionsGUI(Player player, int page, AbstractSpongeChestGUI prevMenu) {
        super(MenuText.SUBMISSIONS, 54, player, page, prevMenu);
    }

    @Override
    protected void build() {
        SpongeSubmissions submissions = SpongeServerSaturday.instance().getSubmissions();
        List<SpongeSubmitter> spongeSubmitters = submissions.getSubmitters();
        List<ItemStack> list = spongeSubmitters.stream().map(SpongeSubmitter::getMenuRepresentation).collect(Collectors.toList());
        setContents(list, (player, itemStack) -> p -> {
            for (SpongeSubmitter submitter : SpongeServerSaturday.instance().getSubmissions().getSubmitters()) {
                if (submitter.getName().equals(itemStack.get(Keys.DISPLAY_NAME).orElse(Text.of()).toPlain())) {
                    new SubmitterGUI(player, submitter, 1, this);
                    return;
                }
            }
        });

        int maxPage = new Double(Math.ceil(list.size() / 45)).intValue();
        setJumpToPage(45, maxPage);
        setPageNavigationButton(48, MenuText.PREVIOUS_PAGE, player -> {
            if (page > 1) {
                new SubmissionsGUI(player, page - 1, prevMenu);
            }
        });

        setPageNavigationButton(50, MenuText.NEXT_PAGE, player -> {
            if (page < maxPage) {
                new SubmissionsGUI(player, page + 1, this);
            }
        });

        setBackButton(53, ItemTypes.BARRIER);
    }
}
