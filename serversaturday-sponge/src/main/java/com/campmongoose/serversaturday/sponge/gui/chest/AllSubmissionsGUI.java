package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.gui.chest.build.EditBuildGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.build.ViewBuildGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class AllSubmissionsGUI extends AbstractSpongePagedGUI {

    public AllSubmissionsGUI(Player player, int page, AbstractSpongeChestGUI prevMenu) {
        super(MenuText.ALL_SUBMISSIONS, 54, player, page, prevMenu);
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

        List<SpongeSubmitter> submitters = submissions.getSubmitters();
        Multimap<SpongeBuild, SpongeSubmitter> map = HashMultimap.create();
        submitters.forEach(submitter -> submitter.getBuilds().forEach(build -> map.put(build, submitter)));
        setContents(new ArrayList<>(map.keySet()), build -> {
            SpongeSubmitter submitter = new ArrayList<>(map.get(build)).get(0);
            return build.getMenuRepresentation(submitter);
        }, build -> {
            SpongeSubmitter submitter = new ArrayList<>(map.get(build)).get(0);
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
                new AllSubmissionsGUI(player, page - 1, prevMenu);
            }
        });

        setPageNavigationButton(50, MenuText.NEXT_PAGE, player -> {
            if (page < maxPage) {
                new AllSubmissionsGUI(player, page + 1, this);
            }
        });

        setBackButton(53, ItemTypes.BARRIER);
    }
}
