package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.sponge.gui.anvil.page.SubmitterJumpToPage;
import com.campmongoose.serversaturday.sponge.gui.chest.build.EditBuildGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.build.ViewBuildGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.List;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

public class SubmitterGUI extends AbstractSpongePagedGUI {

    private final SpongeSubmitter submitter;

    public SubmitterGUI(Player player, SpongeSubmitter submitter, int page, AbstractSpongeChestGUI prevMenu) {
        super(MenuText.submitterMenu(submitter), 54, player, page, prevMenu);
        this.submitter = submitter;
    }

    @Override
    protected void build() {
        List<SpongeBuild> list = submitter.getBuilds();
        setContents(list, build -> build.getMenuRepresentation(submitter), build -> player -> {
            if (player.getUniqueId().equals(this.player.getUniqueId())) {
                new EditBuildGUI(build, submitter, player, this);
            }
            else {
                new ViewBuildGUI(build, submitter, player, this);
            }
        });

        int maxPage = new Double(Math.ceil(list.size() / 45)).intValue();
        ItemStack jumpStack = createItem(ItemTypes.BOOK, Text.of(MenuText.JUMP_PAGE));
        jumpStack.setQuantity(page);
        set(45, jumpStack, player -> new SubmitterJumpToPage(player, this, maxPage, submitter));
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
