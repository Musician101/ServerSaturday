package com.campmongoose.serversaturday.sponge.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.SpongeDescriptionChangeHandler;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.menu.textinput.NameChangeTextInput;
import com.campmongoose.serversaturday.sponge.menu.textinput.ResourcePackChangeMenu;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.GoldenApples;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.BookView;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class BuildMenu extends AbstractSpongeChestMenu {

    private final SpongeBuild build;
    private final SpongeSubmitter submitter;

    public BuildMenu(SpongeBuild build, SpongeSubmitter submitter, Player player, AbstractSpongeChestMenu prevMenu) {
        this(build, submitter, player, prevMenu, false);
    }

    public BuildMenu(SpongeBuild build, SpongeSubmitter submitter, Player player, AbstractSpongeChestMenu prevMenu, boolean manualOpen) {
        super(build.getName(), 9, player, prevMenu, manualOpen);
        this.build = build;
        this.submitter = submitter;
    }

    @Override
    protected void build() {
        Location<World> location = build.getLocation();
        if (player.getUniqueId().equals(submitter.getUUID())) {
            set(0, ItemStack.builder().itemType(ItemTypes.PAPER).quantity(1).add(Keys.DISPLAY_NAME, Text.of(MenuText.RENAME_NAME)).add(Keys.ITEM_LORE, Collections.singletonList(Text.of(MenuText.RENAME_DESC))).build(),
                    player -> new NameChangeTextInput(build, player, this));
            set(1, ItemStack.builder().itemType(ItemTypes.COMPASS).quantity(1).add(Keys.DISPLAY_NAME, Text.of(MenuText.CHANGE_LOCATION_NAME)).add(Keys.ITEM_LORE, MenuText.CHANGE_LOCATION_DESC.stream().map(Text::of).collect(Collectors.toList())).build(),
                    player -> {
                        build.setLocation(player.getLocation());
                        new BuildMenu(build, submitter, player, prevMenu);
                        player.sendMessage(Text.builder(Messages.locationChanged(build)).color(TextColors.GREEN).build());
                    });
            set(2, ItemStack.builder().itemType(ItemTypes.BOOK).quantity(1).add(Keys.DISPLAY_NAME, Text.of(MenuText.CHANGE_DESCRIPTION_NAME)).add(Keys.ITEM_LORE, Collections.singletonList(Text.of(MenuText.CHANGE_DESCRIPTION_DESC))).build(),
                    player -> {
                        SpongeDescriptionChangeHandler sdch = SpongeServerSaturday.instance().getDescriptionChangeHandler();
                        if (sdch.containsPlayer(player)) {
                            player.sendMessage(Text.builder(Messages.EDIT_IN_PROGRESS).color(TextColors.RED).build());
                            return;
                        }

                        sdch.add(player, build);
                    });
            set(3, ItemStack.builder().itemType(ItemTypes.PAINTING).quantity(1).add(Keys.DISPLAY_NAME, Text.of(MenuText.CHANGE_RESOURCE_PACK_NAME)).add(Keys.ITEM_LORE, MenuText.CHANGE_RESOURCE_PACK_DESC.stream().map(Text::of).collect(Collectors.toList())).build(),
                    player -> new ResourcePackChangeMenu(build, player, this));
            set(4, ItemStack.builder().itemType(ItemTypes.FLINT_AND_STEEL).quantity(1).add(Keys.DISPLAY_NAME, Text.of(MenuText.SUBMIT_UNREADY_NAME, build.submitted())).add(Keys.ITEM_LORE, MenuText.SUBMIT_UNREADY_DESC.stream().map(Text::of).collect(Collectors.toList())).build(),
                    player -> {
                        build.setSubmitted(!build.submitted());
                        new BuildMenu(build, submitter, player, prevMenu);
                    });
            set(5, ItemStack.builder().itemType(ItemTypes.COMPASS).quantity(1).add(Keys.DISPLAY_NAME, Text.of(MenuText.TELEPORT_NAME)).add(Keys.ITEM_LORE, Stream.of(MenuText.teleportDesc(location.getExtent().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ())).map(Text::of).collect(Collectors.toList())).build(),
                    player -> {
                        if (player.hasPermission(Permissions.VIEW_GOTO)) {
                            player.setLocation(build.getLocation());
                            player.sendMessage(Text.builder(Messages.teleportedToBuild(build)).color(TextColors.GOLD).build());
                            return;
                        }

                        player.sendMessage(Text.builder(Messages.NO_PERMISSION).color(TextColors.RED).build());
                    });
            set(6, ItemStack.builder().itemType(ItemTypes.BARRIER).quantity(1).add(Keys.DISPLAY_NAME, Text.of(MenuText.DELETE_NAME)).add(Keys.ITEM_LORE, MenuText.DELETE_DESC.stream().map(Text::of).collect(Collectors.toList())).build(),
                    player -> {
                        submitter.removeBuild(build.getName());
                        if (prevMenu == null) {
                            close();
                        }
                        else {
                            prevMenu.open();
                        }
                    });

            if (player.hasPermission(Permissions.FEATURE)) {
                ItemStack.Builder isb = ItemStack.builder().itemType(ItemTypes.GOLDEN_APPLE).quantity(1).add(Keys.DISPLAY_NAME, Text.of(MenuText.FEATURE_NAME)).add(Keys.ITEM_LORE, MenuText.FEATURE_DESC.stream().map(Text::of).collect(Collectors.toList()));
                if (build.featured()) {
                    set(7, isb.add(Keys.GOLDEN_APPLE_TYPE, GoldenApples.ENCHANTED_GOLDEN_APPLE).build(), player -> {
                        build.setFeatured(!build.featured());
                        new BuildMenu(build, submitter, player, prevMenu);
                    });
                }
                else {
                    set(7, isb.build(), player -> {
                        build.setFeatured(!build.featured());
                        new BuildMenu(build, submitter, player, prevMenu);
                    });
                }
            }
        }
        else {
            set(0, ItemStack.builder().itemType(ItemTypes.COMPASS).quantity(1).add(Keys.DISPLAY_NAME, Text.of(MenuText.TELEPORT_NAME)).add(Keys.ITEM_LORE, Stream.of(MenuText.teleportDesc(location.getExtent().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ())).map(Text::of).collect(Collectors.toList())).build(),
                    player -> {
                        if (player.hasPermission(Permissions.VIEW_GOTO)) {
                            player.setLocation(build.getLocation());
                            player.sendMessage(Text.builder(Messages.teleportedToBuild(build)).color(TextColors.GOLD).build());
                        }
                        else {
                            player.sendMessage(Text.builder(Messages.NO_PERMISSION).color(TextColors.RED).build());
                        }
                    });
            set(1, ItemStack.builder().itemType(ItemTypes.BOOK).quantity(1).add(Keys.DISPLAY_NAME, Text.of(MenuText.DESCRIPTION_NAME)).build(),
                    player -> {
                        BookView.Builder bvb = BookView.builder();
                        bvb.author(Text.of(submitter.getName()));
                        bvb.addPages(build.getDescription().stream().map(Text::of).collect(Collectors.toList()));
                        bvb.title(Text.of(build.getName()));
                        player.sendBookView(bvb.build());
                    });
            set(2, ItemStack.builder().itemType(ItemTypes.PAINTING).quantity(1).add(Keys.DISPLAY_NAME, Text.of(MenuText.RESOURCE_PACK_NAME)).add(Keys.ITEM_LORE, Collections.singletonList(Text.of(build.getResourcePack()))).build());
            if (player.hasPermission(Permissions.FEATURE)) {
                ItemStack.Builder isb = ItemStack.builder().itemType(ItemTypes.GOLDEN_APPLE).quantity(1).add(Keys.DISPLAY_NAME, Text.of(MenuText.FEATURE_NAME)).add(Keys.ITEM_LORE, MenuText.FEATURE_DESC.stream().map(Text::of).collect(Collectors.toList()));
                if (build.featured()) {
                    isb.add(Keys.GOLDEN_APPLE_TYPE, GoldenApples.ENCHANTED_GOLDEN_APPLE).build();
                }

                set(3, isb.build(), player -> {
                    build.setFeatured(!build.featured());
                    new BuildMenu(build, submitter, player, prevMenu);
                });
            }
        }

        set(8, ItemStack.builder().itemType(ItemTypes.ARROW).quantity(1).add(Keys.DISPLAY_NAME, Text.of(MenuText.BACK)).build(), player -> {
            if (prevMenu == null) {
                close();
            }
            else {
                prevMenu.open();
            }
        });
    }
}
