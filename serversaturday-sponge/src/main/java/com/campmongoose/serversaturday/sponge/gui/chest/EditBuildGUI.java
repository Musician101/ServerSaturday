package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.gui.SpongeBookGUI;
import com.campmongoose.serversaturday.sponge.gui.SpongeIconBuilder;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import com.campmongoose.serversaturday.sponge.textinput.SpongeTextInput;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.api.item.inventory.property.EquipmentSlotType;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class EditBuildGUI extends BuildGUI {

    public EditBuildGUI(@Nonnull SpongeBuild build, @Nonnull SpongeSubmitter submitter, @Nonnull Player player) {
        super(build, 7, 5, player);
        setButton(0, SpongeIconBuilder.builder(ItemTypes.PAPER).name(Text.of(MenuText.RENAME_NAME)).description(Text.of(MenuText.RENAME_DESC)).build(), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> {
            p.closeInventory();
            p.sendMessage(Text.of(TextColors.GREEN, Messages.SET_BUILD_NAME));
            SpongeTextInput.addPlayer(p, (ply, s) -> {
                submitter.renameBuild(s, build);
                new EditBuildGUI(build, submitter, ply);
            });
        }));
        updateLocation(build);
        setButton(2, SpongeIconBuilder.builder(ItemTypes.BOOK).name(Text.of(MenuText.CHANGE_DESCRIPTION_NAME)).description(Text.of(MenuText.CHANGE_DESCRIPTION_DESC)).build(), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> {
            ItemStack itemStack = player.getInventory().query(QueryOperationTypes.INVENTORY_PROPERTY.of(new EquipmentSlotType(EquipmentTypes.MAIN_HAND)));
            if (itemStack.getType() != ItemTypes.AIR) {
                p.sendMessage(Text.of(TextColors.RED, Messages.HAND_NOT_EMPTY));
                return;
            }

            if (SpongeBookGUI.isEditing(p)) {
                p.sendMessage(Text.of(TextColors.RED, Messages.EDIT_IN_PROGRESS));
                return;
            }

            p.closeInventory();
            new SpongeBookGUI(p, build, build.getDescription(), (ply, pages) -> {
                build.setDescription(pages);
                new EditBuildGUI(build, submitter, ply);
            });
        }));
        setButton(3, SpongeIconBuilder.builder(ItemTypes.PAINTING).name(Text.of(MenuText.CHANGE_RESOURCE_PACKS_NAME)).description(Text.of(MenuText.CHANGE_RESOURCES_PACK_DESC)).build(), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> {
            ItemStack itemStack = player.getInventory().query(QueryOperationTypes.INVENTORY_PROPERTY.of(new EquipmentSlotType(EquipmentTypes.MAIN_HAND)));
            if (itemStack.getType() != ItemTypes.AIR) {
                p.sendMessage(Text.of(TextColors.RED + Messages.HAND_NOT_EMPTY));
                return;
            }

            if (SpongeBookGUI.isEditing(p)) {
                p.sendMessage(Text.of(TextColors.RED + Messages.EDIT_IN_PROGRESS));
                return;
            }

            p.closeInventory();
            new SpongeBookGUI(p, build, build.getResourcePacks(), (ply, pages) -> {
                build.setResourcePacks(pages);
                new EditBuildGUI(build, submitter, ply);
            });
        }));
        updateSubmitted(build);
        setButton(8, SpongeIconBuilder.of(ItemTypes.BARRIER, Text.of("Back")), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> new SubmitterGUI(submitter, p)));
    }

    private void updateLocation(@Nonnull SpongeBuild build) {
        setButton(1, SpongeIconBuilder.builder(ItemTypes.COMPASS).name(Text.of(MenuText.CHANGE_LOCATION_NAME)).description(Text.of(MenuText.CHANGE_LOCATION_DESC)).build(), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> {
            build.setLocation(p.getLocation());
            updateLocation(build);
            p.sendMessage(Text.of(TextColors.GREEN + Messages.locationChanged(build)));
        }));
    }

    private void updateSubmitted(@Nonnull SpongeBuild build) {
        List<Text> submittedDescription = Stream.concat(Stream.of(Text.of(TextColors.GOLD, "Has been submitted? ", (build.submitted() ? Text.of(TextColors.GREEN, "Yes") : Text.of(TextColors.RED, "No")))), MenuText.SUBMIT_UNREADY_DESC.stream().map(Text::of)).collect(Collectors.toList());
        setButton(4, SpongeIconBuilder.builder(ItemTypes.FLINT_AND_STEEL).name(Text.of(MenuText.SUBMIT_UNREADY_NAME)).description(submittedDescription).build(), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> {
            build.setSubmitted(!build.submitted());
            updateSubmitted(build);
        }));
    }
}
