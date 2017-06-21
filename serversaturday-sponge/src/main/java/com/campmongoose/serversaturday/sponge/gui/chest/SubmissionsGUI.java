package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.List;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SubmissionsGUI extends AbstractSpongePagedGUI {

    public SubmissionsGUI(Player player, int page, AbstractSpongeChestGUI prevMenu) {
        super(MenuText.SUBMISSIONS, 54, player, page, prevMenu);
    }

    @Override
    protected void build() {
        SpongeSubmissions submissions;
        try {
            submissions = SpongeServerSaturday.instance().getSubmissions();
        }
        catch (SubmissionsNotLoadedException e) {
            player.closeInventory(generatePluginCause());
            player.sendMessage(Text.builder(e.getMessage()).color(TextColors.RED).build());
            return;
        }

        List<SpongeSubmitter> spongeSubmitters = submissions.getSubmitters();
        setContents(spongeSubmitters, SpongeSubmitter::getMenuRepresentation, submitter -> player -> new SubmitterGUI(player, submitter, 1, this));
        int maxPage = new Double(Math.ceil(spongeSubmitters.size() / 45)).intValue();
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
