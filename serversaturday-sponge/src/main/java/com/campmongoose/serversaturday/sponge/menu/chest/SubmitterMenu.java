package com.campmongoose.serversaturday.sponge.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.sponge.menu.textinput.JumpToPage;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.List;
import java.util.stream.Collectors;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

public class SubmitterMenu extends AbstractSpongeChestMenu {

    private final int page;
    private final SpongeSubmitter submitter;

    public SubmitterMenu(Player player, SpongeSubmitter submitter, int page, AbstractSpongeChestMenu prevMenu) {
        super(MenuText.submitterMenu(submitter), 54, player, prevMenu);
        this.page = page;
        this.submitter = submitter;
    }

    @Override
    protected void build() {
        List<ItemStack> list = submitter.getBuilds().stream().map(build -> build.getMenuRepresentation(submitter)).collect(Collectors.toList());
        for (int x = 0; x < 54; x++) {
            int subListPosition = x + (page - 1) * 45;
            if (x < 45 && list.size() > subListPosition) {
                ItemStack itemStack = list.get(subListPosition);
                set(x, itemStack, player -> {
                    for (SpongeBuild build : submitter.getBuilds()) {
                        if (build.getName().equals(itemStack.get(Keys.DISPLAY_NAME).orElse(Text.of()).toPlain())) {
                            new BuildMenu(build, submitter, player, this);
                        }
                    }
                });
            }
        }

        ItemStack prevPage = ItemStack.of(ItemTypes.ARROW, 1);
        prevPage.offer(Keys.DISPLAY_NAME, Text.of(MenuText.PREVIOUS_PAGE));
        set(49, prevPage, player -> {
            if (page - 1 > 0) {
                new SubmitterMenu(player, submitter, page - 1, prevMenu);
            }
        });

        ItemStack jumpPage = ItemStack.of(ItemTypes.BOOK, 1);
        jumpPage.offer(Keys.DISPLAY_NAME, Text.of(MenuText.JUMP_PAGE));
        set(49, jumpPage, player -> new JumpToPage(player, this));

        ItemStack nextPage = ItemStack.of(ItemTypes.ARROW, 1);
        prevPage.offer(Keys.DISPLAY_NAME, Text.of(MenuText.NEXT_PAGE));
        set(53, nextPage, player -> {
            if (page + 1 > Integer.MAX_VALUE) {
                new SubmitterMenu(player, submitter, page + 1, prevMenu);
            }
        });
    }
}
