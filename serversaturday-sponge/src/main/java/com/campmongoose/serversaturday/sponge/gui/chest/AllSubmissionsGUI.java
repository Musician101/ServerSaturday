package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class AllSubmissionsGUI extends AbstractSpongePagedGUI {

    public AllSubmissionsGUI(Player player, int page, AbstractSpongeChestGUI prevMenu) {
        super(MenuText.ALL_SUBMISSIONS, 54, player, page, prevMenu);
    }

    @Override
    protected void build() {
        List<ItemStack> list = new ArrayList<>();
        SpongeServerSaturday.instance().getSubmissions().getSubmitters().forEach(submitter ->
                list.addAll(submitter.getBuilds().stream().map(build ->
                        build.getMenuRepresentation(submitter)).collect(Collectors.toList())));

        setContents(list, (p, itemStack) -> {
            return player -> {
                String submitterName = itemStack.get(Keys.ITEM_LORE).orElseThrow(IllegalArgumentException::new).get(0).toPlain();
                SpongeServerSaturday plugin = SpongeServerSaturday.instance();
                SpongeSubmitter submitter = null;
                SpongeSubmissions submissions = plugin.getSubmissions();
                UUID uuid = plugin.getUUIDCache().getUUIDOf(submitterName);
                if (uuid != null) {
                    submitter = submissions.getSubmitter(uuid);
                }
                else {
                    for (SpongeSubmitter s : submissions.getSubmitters()) {
                        if (submitterName.equals(s.getName())) {
                            submitter = s;
                        }
                    }
                }

                if (submitter == null) {
                    player.sendMessage(Text.builder(Messages.PLAYER_NOT_FOUND).color(TextColors.RED).build());
                    return;
                }

                new SubmitterGUI(player, submitter, 1, this);
            };
        });

        int maxPage = new Double(Math.ceil(list.size() / 45)).intValue();
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
