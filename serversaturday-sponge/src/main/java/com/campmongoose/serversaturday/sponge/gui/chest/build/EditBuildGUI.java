package com.campmongoose.serversaturday.sponge.gui.chest.build;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.gui.anvil.AnvilGUI;
import com.campmongoose.serversaturday.sponge.gui.book.BookGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.AbstractSpongeChestGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.Collections;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class EditBuildGUI extends BuildGUI {

    public EditBuildGUI(@Nonnull SpongeBuild build, @Nonnull SpongeSubmitter submitter, Player player, @Nullable AbstractSpongeChestGUI prevMenu) {
        super(build, submitter, player, prevMenu, true);
        if (submitter.getUUID().equals(player.getUniqueId())) {
            open();
        }
    }

    @Override
    protected void build() {
        //TODO test all menu buttons, and commands
        set(0, createItem(ItemTypes.PAPER, Text.of(MenuText.RENAME_NAME), Text.of(MenuText.RENAME_DESC)), player -> {
            new AnvilGUI(player, (ep, s) -> {
                build.setName(s);
                open();
                return null;
            });
        });
        set(1, createItem(ItemTypes.COMPASS, Text.of(MenuText.CHANGE_LOCATION_NAME), MenuText.CHANGE_LOCATION_DESC.stream().map(Text::of).toArray(Text[]::new)));
        set(2, createItem(ItemTypes.BOOK, Text.of(MenuText.CHANGE_DESCRIPTION_NAME, Text.of(MenuText.CHANGE_DESCRIPTION_DESC))), player -> {
            ItemStack itemStack = player.getItemInHand(HandTypes.MAIN_HAND).orElse(ItemStack.empty());
            if (itemStack.getItem() == ItemTypes.NONE) {
                player.sendMessage(Text.builder(Messages.HAND_NOT_EMPTY).color(TextColors.RED).build());
                return;
            }

            if (BookGUI.isEditing(player)) {
                player.sendMessage(Text.builder(Messages.EDIT_IN_PROGRESS).color(TextColors.RED).build());
                return;
            }

            new BookGUI(player, build, build.getDescription(), pages -> {
                build.setDescription(pages);
                open();
            });
        });
        set(3, createItem(ItemTypes.PAINTING, Text.of(MenuText.CHANGE_RESOURCE_PACK_NAME), MenuText.CHANGE_RESOURCE_PACK_DESC.stream().map(Text::of).toArray(Text[]::new)), player -> {
            ItemStack itemStack = player.getItemInHand(HandTypes.MAIN_HAND).orElse(ItemStack.empty());
            if (itemStack.getItem() == ItemTypes.NONE) {
                player.sendMessage(Text.builder(Messages.HAND_NOT_EMPTY).color(TextColors.RED).build());
                return;
            }

            if (BookGUI.isEditing(player)) {
                player.sendMessage(Text.builder(Messages.EDIT_IN_PROGRESS).color(TextColors.RED).build());
                return;
            }

            new BookGUI(player, build, build.getResourcePack(), pages -> {
                build.setResourcePack(pages);
                open();
            });
        });

        ItemStack submit = createItem(ItemTypes.FLINT_AND_STEEL, Text.of(MenuText.SUBMIT_UNREADY_NAME), MenuText.SUBMIT_UNREADY_DESC.stream().map(Text::of).toArray(Text[]::new));
        if (build.submitted()) {
            submit.offer(Keys.ITEM_ENCHANTMENTS, Collections.singletonList(new ItemEnchantment(Enchantments.UNBREAKING, 1)));
            submit.offer(Keys.HIDE_ENCHANTMENTS, true);
        }

        set(4, submit, player -> {
            build.setSubmitted(!build.submitted());
            open();
        });

        setTeleportButton(5, player.getLocation());
        set(6, createItem(ItemTypes.BARRIER, Text.of(MenuText.DELETE_NAME), MenuText.DELETE_DESC.stream().map(Text::of).toArray(Text[]::new)),
                player -> {
                    submitter.removeBuild(build.getName());
                    player.closeInventory(generatePluginCause());
                    if (prevMenu != null) {
                        Task.builder().delayTicks(1).name("SS-EditBuildGUI").execute(prevMenu::open).submit(SpongeServerSaturday.instance());
                    }
                });

        setFeatureButton(7);
        setBackButton(8, ItemTypes.ARROW);
    }
}
