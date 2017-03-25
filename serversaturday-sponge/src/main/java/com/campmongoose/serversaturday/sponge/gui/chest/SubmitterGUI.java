package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.List;
import java.util.stream.Collectors;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

public class SubmitterGUI extends AbstractSpongePagedGUI {

    private final SpongeSubmitter submitter;

    public SubmitterGUI(Player player, SpongeSubmitter submitter, int page, AbstractSpongeChestGUI prevMenu) {
        super(MenuText.submitterMenu(submitter), 54, player, page, prevMenu);
        this.submitter = submitter;
    }

    @Override
    protected void build() {
        List<ItemStack> list = submitter.getBuilds().stream().map(build -> build.getMenuRepresentation(submitter)).collect(Collectors.toList());
        setContents(list, (player, itemStack) -> p -> {
            for (SpongeBuild build : submitter.getBuilds()) {
                if (build.getName().equals(itemStack.get(Keys.DISPLAY_NAME).orElseThrow(IllegalArgumentException::new).toPlain())) {
                    new BuildGUI(build, submitter, player, this);
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
