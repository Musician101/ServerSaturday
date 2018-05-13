package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.common.gui.chest.ChestGUIs;
import com.campmongoose.serversaturday.common.gui.chest.GUIButton;
import com.campmongoose.serversaturday.common.submission.Submissions;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.gui.SpongeBookGUI;
import com.campmongoose.serversaturday.sponge.gui.SpongeIconBuilder;
import com.campmongoose.serversaturday.sponge.gui.anvil.AnvilGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.BookView;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SpongeChestGUIs extends ChestGUIs<SpongeChestGUIBuilder, Class<? extends ClickInventoryEvent>, SpongeChestGUI, Inventory, SpongeBuild, Location<World>, Player, ItemStack, Text, SpongeSubmitter> {

    public static final ChestGUIs<SpongeChestGUIBuilder, Class<? extends ClickInventoryEvent>, SpongeChestGUI, Inventory, SpongeBuild, Location<World>, Player, ItemStack, Text, SpongeSubmitter> INSTANCE = new SpongeChestGUIs();

    private SpongeChestGUIs() {

    }

    @Nonnull
    @Override
    public Optional<SpongeChestGUI> allSubmissions(int page, @Nonnull Player player, @Nullable SpongeChestGUI prevGUI) {
        Multimap<SpongeBuild, SpongeSubmitter> map = HashMultimap.create();
        SpongeServerSaturday.instance().map(ServerSaturday::getSubmissions).map(Submissions::getSubmitters).ifPresent(submitters -> submitters.forEach(submitter -> submitter.getBuilds().forEach(build -> map.put(build, submitter))));
        return Optional.of(paged(Text.of(MenuText.ALL_SUBMISSIONS), player, prevGUI, page, ClickInventoryEvent.Primary.class, new ArrayList<>(map.keySet()), build -> {
            SpongeSubmitter submitter = new ArrayList<>(map.get(build)).get(0);
            return build.getMenuRepresentation(submitter);
        }, (p, build) -> {
            SpongeSubmitter submitter = new ArrayList<>(map.get(build)).get(0);
            map.remove(build, submitter);
            return (g, ply) -> {
                if (ply.getUniqueId().equals(submitter.getUUID())) {
                    editBuild(build, submitter, ply, g);
                }
                else {
                    viewBuild(build, submitter, ply, g);
                }
            };
        }, (p, i) -> allSubmissions(i, p, prevGUI)).build());
    }

    @Nonnull
    @Override
    protected SpongeChestGUIBuilder build(int featureSlot, int teleportSlot, @Nonnull SpongeBuild build, @Nonnull Player player, @Nullable SpongeChestGUI prevGUI) {
        Location<World> location = build.getLocation();
        SpongeChestGUIBuilder builder = builder(9, 8, Text.of(build.getName()), player, prevGUI).setButton(new GUIButton<>(teleportSlot, ClickInventoryEvent.Primary.class, SpongeIconBuilder.builder(ItemTypes.COMPASS).name(Text.of(MenuText.TELEPORT_NAME)).description(MenuText.teleportDesc(location.getExtent().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()).stream().map(Text::of).collect(Collectors.toList())).build(), (g, p) -> {
            if (player.hasPermission(Permissions.VIEW_GOTO)) {
                player.setLocation(location);
                player.sendMessage(Text.of(TextColors.GREEN, Messages.teleportedToBuild(build)));
                return;
            }

            player.sendMessage(Text.of(TextColors.RED, Messages.NO_PERMISSION));
        }));

        if (player.hasPermission(Permissions.FEATURE)) {
            builder.setButton(new GUIButton<>(featureSlot, ClickInventoryEvent.Primary.class, SpongeIconBuilder.builder(ItemTypes.GOLDEN_APPLE).name(Text.of(MenuText.FEATURE_NAME)).description(MenuText.FEATURE_DESC.stream().map(Text::of).collect(Collectors.toList())).addGlow(build.featured()).build(), (g, p) -> {
                build.setFeatured(!build.featured());
                g.open();
            }));
        }

        return builder;
    }

    @Nonnull
    @Override
    protected SpongeChestGUIBuilder builder(int size, int backButtonSlot, @Nonnull Text name, @Nonnull Player player, @Nullable SpongeChestGUI prevGUI) {
        return SpongeChestGUI.builder().setSize(size).setName(name).setPlayer(player).setPreviousGUI(prevGUI).setBackButton(backButtonSlot, ClickInventoryEvent.Primary.class);
    }

    @Nonnull
    @Override
    public Optional<SpongeChestGUI> editBuild(@Nonnull SpongeBuild build, @Nonnull SpongeSubmitter submitter, @Nonnull Player player, @Nullable SpongeChestGUI prevGUI) {
        return Optional.of(build(7, 5, build, player, prevGUI).setButton(new GUIButton<>(0, ClickInventoryEvent.Primary.class,
                SpongeIconBuilder.builder(ItemTypes.PAPER).name(Text.of(MenuText.RENAME_NAME)).description(Text.of(MenuText.RENAME_DESC)).build(),
                (g, p) -> new AnvilGUI(player, (ply, s) -> {
                    submitter.renameBuild(s, build);
                    editBuild(build, submitter, (Player) ply, prevGUI);
                    return null;
                })))
                .setButton(new GUIButton<>(1, ClickInventoryEvent.Primary.class, SpongeIconBuilder.builder(ItemTypes.COMPASS).name(Text.of(MenuText.CHANGE_LOCATION_NAME)).description(MenuText.CHANGE_LOCATION_DESC.stream().map(Text::of).collect(Collectors.toList())).build(), (g, p) -> {
                    build.setLocation(player.getLocation());
                    g.open();
                    player.sendMessage(Text.of(TextColors.GREEN, Messages.locationChanged(build)));
                })).setButton(new GUIButton<>(2, ClickInventoryEvent.Primary.class, SpongeIconBuilder.builder(ItemTypes.BOOK).name(Text.of(MenuText.CHANGE_DESCRIPTION_NAME)).description(Text.of(MenuText.CHANGE_DESCRIPTION_DESC)).build(), (g, p) -> {
                    Optional<ItemStack> itemStack = p.getItemInHand(HandTypes.MAIN_HAND);
                    if (itemStack.isPresent() && itemStack.get().getType() != ItemTypes.AIR) {
                        player.sendMessage(Text.of(TextColors.RED, Messages.HAND_NOT_EMPTY));
                        return;
                    }

                    if (SpongeBookGUI.isEditing(p)) {
                        player.sendMessage(Text.of(TextColors.RED, Messages.EDIT_IN_PROGRESS));
                        return;
                    }

                    p.closeInventory();
                    new SpongeBookGUI(p, build, build.getDescription().stream().map(Text::of).collect(Collectors.toList()), pages -> {
                        build.setDescription(pages);
                        g.open();
                    });
                })).setButton(new GUIButton<>(3, ClickInventoryEvent.Primary.class, SpongeIconBuilder.builder(ItemTypes.PAINTING).name(Text.of(MenuText.CHANGE_RESOURCE_PACKS_NAME)).description(MenuText.CHANGE_RESOURCES_PACK_DESC.stream().map(Text::of).collect(Collectors.toList())).build(), (g, p) -> {
                    Optional<ItemStack> itemStack = p.getItemInHand(HandTypes.MAIN_HAND);
                    if (itemStack.isPresent() && itemStack.get().getType() != ItemTypes.AIR) {
                        player.sendMessage(Text.of(TextColors.RED, Messages.HAND_NOT_EMPTY));
                        return;
                    }

                    if (SpongeBookGUI.isEditing(p)) {
                        player.sendMessage(Text.of(TextColors.RED, Messages.EDIT_IN_PROGRESS));
                        return;
                    }

                    p.closeInventory();
                    new SpongeBookGUI(p, build, build.getResourcePacks().stream().map(Text::of).collect(Collectors.toList()), pages -> {
                        build.setResourcePacks(pages);
                        g.open();
                    });
                })).setButton(new GUIButton<>(4, ClickInventoryEvent.Primary.class, SpongeIconBuilder.builder(ItemTypes.FLINT_AND_STEEL).name(Text.of(MenuText.SUBMIT_UNREADY_NAME)).description(MenuText.SUBMIT_UNREADY_DESC.stream().map(Text::of).collect(Collectors.toList())).addGlow(build.submitted()).build(), (g, p) -> {
                    build.setSubmitted(!build.submitted());
                    g.open();
                })).setButton(new GUIButton<>(6, ClickInventoryEvent.Primary.class, SpongeIconBuilder.builder(ItemTypes.ENDER_CHEST).name(Text.of(MenuText.DELETE_NAME)).description(MenuText.DELETE_DESC.stream().map(Text::of).collect(Collectors.toList())).build(), (g, p) -> {
                    submitter.removeBuild(build.getName());
                    if (prevGUI != null) {
                        prevGUI.open();
                    }
                })).build()).filter(gui -> player.getUniqueId().equals(submitter.getUUID()));
    }

    @Nonnull
    @Override
    protected <O> SpongeChestGUIBuilder paged(@Nonnull Text name, @Nonnull Player player, @Nullable SpongeChestGUI prevGUI, int page, @Nonnull Class<? extends ClickInventoryEvent> clickType, @Nonnull List<O> contents, @Nonnull Function<O, ItemStack> itemStackMapper, @Nullable BiFunction<Player, O, BiConsumer<SpongeChestGUI, Player>> actionMapper, @Nonnull BiConsumer<Player, Integer> pageNavigator) {
        int maxPage = new Double(Math.ceil(contents.size() / 45)).intValue();
        return builder(54, 53, name, player, prevGUI).setPage(page).setContents(ClickInventoryEvent.Primary.class, contents, itemStackMapper, actionMapper)
                .setJumpToPage(45, maxPage, pageNavigator).setPageNavigation(48, Text.of(MenuText.PREVIOUS_PAGE), (gui, p) -> {
                    if (page > 1) {
                        pageNavigator.accept(p, page - 1);
                    }
                })
                .setPageNavigation(50, Text.of(MenuText.NEXT_PAGE), (gui, p) -> {
                    if (page + 1 > maxPage) {
                        pageNavigator.accept(p, page + 1);
                    }
                });
    }

    @Nonnull
    @Override
    public Optional<SpongeChestGUI> submissions(int page, @Nonnull Player player, @Nullable SpongeChestGUI prevGUI) {
        return Optional.of(paged(Text.of(MenuText.SUBMISSIONS), player, prevGUI, page, ClickInventoryEvent.Primary.class, SpongeServerSaturday.instance().map(ServerSaturday::getSubmissions).map(Submissions::getSubmitters).orElse(Collections.emptyList()), SpongeSubmitter::getMenuRepresentation, (p, submitter) -> (g, ply) -> submitter(1, ply, submitter, g), (p, i) -> submissions(i, p, prevGUI)).build());
    }

    @Nonnull
    @Override
    public Optional<SpongeChestGUI> submitter(int page, @Nonnull Player player, @Nonnull SpongeSubmitter submitter, @Nullable SpongeChestGUI prevGUI) {
        return Optional.of(paged(Text.of(MenuText.submitterMenu(submitter)), player, prevGUI, page, ClickInventoryEvent.Primary.class, submitter.getBuilds(), build -> build.getMenuRepresentation(submitter), (p, build) -> (g, ply) -> {
            if (ply.getUniqueId().equals(submitter.getUUID())) {
                editBuild(build, submitter, player, g);
            }
            else {
                viewBuild(build, submitter, player, g);
            }
        }, (p, i) -> submitter(i, p, submitter, prevGUI)).build());
    }

    @Nonnull
    @Override
    public Optional<SpongeChestGUI> viewBuild(@Nonnull SpongeBuild build, @Nonnull SpongeSubmitter submitter, @Nonnull Player player, @Nullable SpongeChestGUI prevGUI) {
        return Optional.of(build(3, 0, build, player, prevGUI).setButton(new GUIButton<>(1, ClickInventoryEvent.Primary.class, SpongeIconBuilder.builder(ItemTypes.BOOK).name(Text.of(MenuText.DESCRIPTION_NAME)).description(Text.of(MenuText.DESCRIPTION_DESC)).build(), (g, p) -> player.sendBookView(BookView.builder().author(Text.of(submitter.getName())).addPages(build.getDescription().stream().map(Text::of).collect(Collectors.toList())).title(Text.of(build.getName())).build()))).setButton(new GUIButton<>(2, ClickInventoryEvent.Primary.class, SpongeIconBuilder.builder(ItemTypes.PAINTING).name(Text.of(MenuText.RESOURCE_PACK_NAME)).description(Text.of(MenuText.RESOURCE_PACK_DESC)).build(), (g, p) -> player.sendBookView(BookView.builder().author(Text.of(submitter.getName())).addPages(build.getDescription().stream().map(Text::of).collect(Collectors.toList())).title(Text.of(build.getName())).build()))).build()).filter(gui -> !player.getUniqueId().equals(submitter.getUUID()));
    }
}
