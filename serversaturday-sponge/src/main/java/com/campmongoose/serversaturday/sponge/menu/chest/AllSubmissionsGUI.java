package com.campmongoose.serversaturday.sponge.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.uuid.UUIDUtils;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.menu.textinput.JumpToPage;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class AllSubmissionsGUI extends AbstractSpongeChestGUI {

    private final int page;

    public AllSubmissionsGUI(Player player, int page, AbstractSpongeChestGUI prevMenu) {
        super(MenuText.ALL_SUBMISSIONS, 54, player, prevMenu);
        this.page = page;
    }

    @Override
    protected void build() {
        List<ItemStack> list = new ArrayList<>();
        SpongeServerSaturday.instance().getSubmissions().getSubmitters().forEach(submitter -> submitter.getBuilds().forEach(build ->
        {
            ItemStack itemStack = ItemStack.of(ItemTypes.BOOK, 1);
            itemStack.offer(Keys.DISPLAY_NAME, Text.of(build.getName()));
            itemStack.offer(Keys.ITEM_LORE, Collections.singletonList(Text.of(submitter.getName())));
            if (build.submitted() && !build.featured()) {
                itemStack.offer(Keys.ITEM_ENCHANTMENTS, Collections.singletonList(new ItemEnchantment(Enchantments.AQUA_AFFINITY, 1)));
                itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
            }

            list.add(itemStack);
        }));

        for (int x = 0; x < 54; x++) {
            int subListPosition = x + (page - 1) * 45;
            if (x < 45 && list.size() > subListPosition) {
                ItemStack itemStack = list.get(subListPosition);
                set(x, itemStack, player -> {
                    String submitterName = itemStack.get(Keys.ITEM_LORE).orElse(Collections.singletonList(Text.of(""))).get(0).toPlain();
                    SpongeSubmitter submitter = null;
                    SpongeSubmissions submissions = SpongeServerSaturday.instance().getSubmissions();
                    try {
                        submitter = submissions.getSubmitter(UUIDUtils.getUUIDOf(submitterName));
                    }
                    catch (IOException exception) {
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
                });
            }
        }

        ItemStack prevPage = ItemStack.of(ItemTypes.ARROW, 1);
        prevPage.offer(Keys.DISPLAY_NAME, Text.of(MenuText.PREVIOUS_PAGE));
        set(48, prevPage, player -> {
            if (page - 1 < 1) {
                new AllSubmissionsGUI(player, page - 1, prevMenu);
            }
        });

        ItemStack jumpPage = ItemStack.of(ItemTypes.BOOK, 1);
        jumpPage.offer(Keys.DISPLAY_NAME, Text.of(MenuText.JUMP_PAGE));
        set(49, prevPage, player -> new JumpToPage(player, prevMenu));

        ItemStack nextPage = ItemStack.of(ItemTypes.ARROW, 1);
        nextPage.offer(Keys.DISPLAY_NAME, Text.of(MenuText.NEXT_PAGE));
        set(50, nextPage, player -> {
            if (page + 1 > Integer.MAX_VALUE) {
                new AllSubmissionsGUI(player, page + 1, prevMenu);
            }
        });
    }
}
