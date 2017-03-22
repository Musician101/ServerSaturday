package com.campmongoose.serversaturday.sponge.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.menu.textinput.JumpToPage;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.List;
import java.util.stream.Collectors;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

public class SubmissionsGUI extends AbstractSpongeChestGUI {

    private final int page;

    public SubmissionsGUI(Player player, int page, AbstractSpongeChestGUI prevMenu) {
        super(MenuText.SUBMISSIONS, 54, player, prevMenu);
        this.page = page;
    }

    @Override
    protected void build() {
        List<ItemStack> list = SpongeServerSaturday.instance().getSubmissions().getSubmitters().stream().map(SpongeSubmitter::getMenuRepresentation).collect(Collectors.toList());
        for (int x = 0; x < 54; x++) {
            int subListPosition = x + (page - 1) * 45;
            if (x < 45 && list.size() > subListPosition) {
                ItemStack itemStack = list.get(subListPosition);
                set(x, itemStack, player -> {
                    for (SpongeSubmitter submitter : SpongeServerSaturday.instance().getSubmissions().getSubmitters()) {
                        if (submitter.getName().equals(itemStack.get(Keys.DISPLAY_NAME).orElse(Text.of()).toPlain())) {
                            new SubmitterGUI(player, submitter, 1, this);
                        }
                    }
                });
            }
        }

        ItemStack prevPage = ItemStack.of(ItemTypes.ARROW, 1);
        prevPage.offer(Keys.DISPLAY_NAME, Text.of(MenuText.PREVIOUS_PAGE));
        set(48, prevPage, player -> {
            if (page - 1 > 0) {
                new SubmissionsGUI(player, page - 1, this);
            }
        });

        ItemStack jumpPage = ItemStack.of(ItemTypes.BOOK, 1);
        jumpPage.offer(Keys.DISPLAY_NAME, Text.of(MenuText.JUMP_PAGE));
        set(49, prevPage, player -> new JumpToPage(player, this));

        ItemStack nextPage = ItemStack.of(ItemTypes.ARROW, 1);
        nextPage.offer(Keys.DISPLAY_NAME, Text.of(MenuText.NEXT_PAGE));
        set(50, nextPage, player -> {
            if (page + 1 > Integer.MAX_VALUE) {
                new SubmissionsGUI(player, page + 1, this);
            }
        });
    }
}
