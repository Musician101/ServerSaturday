package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.google.common.collect.ImmutableMap;
import io.musician101.musicianlibrary.java.minecraft.sponge.SpongeTextInput;
import io.musician101.musicianlibrary.java.minecraft.sponge.gui.book.SpongeBookGUI;
import io.musician101.musicianlibrary.java.minecraft.sponge.gui.chest.SpongeIconBuilder;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.menu.ClickTypes;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.plugin.PluginContainer;

public class EditBuildGUI extends BuildGUI {

    public EditBuildGUI(@Nonnull Build<Component> build, @Nonnull Submitter<Component> submitter, @Nonnull ServerPlayer player) {
        super(build, 7, 5, player);
        setButton(0, SpongeIconBuilder.builder(ItemTypes.PAPER).name(Component.text(MenuText.RENAME_NAME)).description(Component.text(MenuText.RENAME_DESC)).build(), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> {
            p.closeInventory();
            p.sendMessage(Component.text(Messages.SET_BUILD_NAME).color(NamedTextColor.GREEN));
            new SpongeTextInput(SpongeServerSaturday.instance().getPluginContainer(), p) {

                @Override
                protected void process(ServerPlayer player, String s) {
                    submitter.renameBuild(s, build);
                    new EditBuildGUI(build, submitter, player);
                }
            };
        }));
        updateLocation(build);
        setButton(2, SpongeIconBuilder.builder(ItemTypes.BOOK).name(Component.text(MenuText.CHANGE_DESCRIPTION_NAME)).description(Component.text(MenuText.CHANGE_DESCRIPTION_DESC)).build(), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> {
            ItemStack itemStack = player.itemInHand(HandTypes.MAIN_HAND);
            if (!itemStack.isEmpty()) {
                p.sendMessage(Component.text(Messages.HAND_NOT_EMPTY).color(NamedTextColor.RED));
                return;
            }

            PluginContainer pluginContainer = SpongeServerSaturday.instance().getPluginContainer();
            if (SpongeBookGUI.isEditing(pluginContainer, p)) {
                p.sendMessage(Component.text(Messages.EDIT_IN_PROGRESS).color(NamedTextColor.RED));
                return;
            }

            p.closeInventory();
            ItemStack book = ItemStack.builder().itemType(ItemTypes.WRITABLE_BOOK).quantity(1).add(Keys.AUTHOR, Component.text(player.name())).add(Keys.DISPLAY_NAME, Component.text(build.getName())).add(Keys.PAGES, build.getDescription()).build();
            new SpongeBookGUI(pluginContainer, p, book, (ply, pages) -> {
                build.setDescription(pages);
                new EditBuildGUI(build, submitter, ply);
            });
        }));
        setButton(3, SpongeIconBuilder.builder(ItemTypes.PAINTING).name(Component.text(MenuText.CHANGE_RESOURCE_PACKS_NAME)).description(MenuText.CHANGE_RESOURCES_PACK_DESC.stream().map(Component::text).collect(Collectors.toList())).build(), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> {
            ItemStack itemStack = player.itemInHand(HandTypes.MAIN_HAND);
            if (!itemStack.isEmpty()) {
                p.sendMessage(Component.text(Messages.HAND_NOT_EMPTY).color(NamedTextColor.RED));
                return;
            }

            PluginContainer pluginContainer = SpongeServerSaturday.instance().getPluginContainer();
            if (SpongeBookGUI.isEditing(pluginContainer, p)) {
                p.sendMessage(Component.text(Messages.EDIT_IN_PROGRESS).color(NamedTextColor.RED));
                return;
            }

            p.closeInventory();
            ItemStack book = ItemStack.builder().itemType(ItemTypes.WRITABLE_BOOK).quantity(1).add(Keys.AUTHOR, Component.text(player.name())).add(Keys.DISPLAY_NAME, Component.text(build.getName())).add(Keys.PAGES, build.getResourcePacks()).build();
            new SpongeBookGUI(pluginContainer, p, book, (ply, pages) -> {
                build.setResourcePacks(pages);
                new EditBuildGUI(build, submitter, ply);
            });
        }));
        updateSubmitted(build);
        setButton(8, SpongeIconBuilder.of(ItemTypes.BARRIER, Component.text("Back")), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> new SubmitterGUI(submitter, p)));
    }

    private void updateLocation(@Nonnull Build<Component> build) {
        setButton(1, SpongeIconBuilder.builder(ItemTypes.COMPASS).name(Component.text(MenuText.CHANGE_LOCATION_NAME)).description(MenuText.CHANGE_LOCATION_DESC.stream().map(Component::text).collect(Collectors.toList())).build(), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> {
            build.setLocation(toLibLocation((ServerLocation) p.location()));
            updateLocation(build);
            p.sendMessage(Component.text(Messages.locationChanged(build)).color(NamedTextColor.GREEN));
        }));
    }

    private void updateSubmitted(@Nonnull Build<Component> build) {
        List<Component> submittedDescription = Stream.concat(Stream.of(Component.join(Component.text(" "), Component.text("Has been submitted? ").color(NamedTextColor.GOLD), (build.submitted() ? Component.text("Yes").color(NamedTextColor.GREEN) : Component.text("No").color(NamedTextColor.RED)))), MenuText.SUBMIT_UNREADY_DESC.stream().map(Component::text)).collect(Collectors.toList());
        setButton(4, SpongeIconBuilder.builder(ItemTypes.FLINT_AND_STEEL).name(Component.text(MenuText.SUBMIT_UNREADY_NAME)).description(submittedDescription).build(), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> {
            build.setSubmitted(!build.submitted());
            updateSubmitted(build);
        }));
    }
}
