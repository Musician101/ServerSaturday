package com.campmongoose.serversaturday.sponge.gui.chest.build;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.SpongeDescriptionChangeHandler;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.gui.chest.AbstractSpongeChestGUI;
import com.campmongoose.serversaturday.sponge.gui.textinput.NameChangeTextInput;
import com.campmongoose.serversaturday.sponge.gui.textinput.ResourcePackChangeTextInput;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.data.type.GoldenApples;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.BookView;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class BuildGUI extends AbstractSpongeChestGUI {

    private final SpongeBuild build;
    private final SpongeSubmitter submitter;

    public BuildGUI(@Nonnull SpongeBuild build, @Nonnull SpongeSubmitter submitter, Player player, @Nullable AbstractSpongeChestGUI prevMenu) {
        this(build, submitter, player, prevMenu, false);
    }

    public BuildGUI(@Nonnull SpongeBuild build, @Nonnull SpongeSubmitter submitter, @Nonnull Player player, @Nullable AbstractSpongeChestGUI prevMenu, boolean manualOpen) {
        super(build.getName(), 9, player, prevMenu, manualOpen);
        this.build = build;
        this.submitter = submitter;
    }

    @Override
    protected void build() {
        Location<World> location = build.getLocation();
        if (player.getUniqueId().equals(submitter.getUUID())) {
            set(0, createItem(ItemTypes.PAPER, Text.of(MenuText.RENAME_NAME), Text.of(MenuText.RENAME_DESC)), player -> {
                //TODO test all text inputs, menu buttons, and commands
                SpongeServerSaturday.instance().getLogger().info("" + player.closeInventory(generatePluginCause()));
                new NameChangeTextInput(build, player, this);
            });
            set(1, createItem(ItemTypes.COMPASS, Text.of(MenuText.CHANGE_LOCATION_NAME), MenuText.CHANGE_LOCATION_DESC.stream().map(Text::of).toArray(Text[]::new)));
            set(2, createItem(ItemTypes.BOOK, Text.of(MenuText.CHANGE_DESCRIPTION_NAME, Text.of(MenuText.CHANGE_DESCRIPTION_DESC))),
                    player -> {
                        SpongeDescriptionChangeHandler sdch = SpongeServerSaturday.instance().getDescriptionChangeHandler();
                        if (sdch.containsPlayer(player)) {
                            player.sendMessage(Text.builder(Messages.EDIT_IN_PROGRESS).color(TextColors.RED).build());
                            return;
                        }

                        sdch.add(player, build);
                    });
            set(3, createItem(ItemTypes.PAINTING, Text.of(MenuText.CHANGE_RESOURCE_PACK_NAME), MenuText.CHANGE_RESOURCE_PACK_DESC.stream().map(Text::of).toArray(Text[]::new)),
                    player -> new ResourcePackChangeTextInput(build, player, this));

            ItemStack submit = createItem(ItemTypes.FLINT_AND_STEEL, Text.of(MenuText.SUBMIT_UNREADY_NAME), MenuText.SUBMIT_UNREADY_DESC.stream().map(Text::of).toArray(Text[]::new));
            if (build.submitted()) {
                submit.offer(Keys.ITEM_ENCHANTMENTS, Collections.singletonList(new ItemEnchantment(Enchantments.UNBREAKING, 1)));
                submit.offer(Keys.HIDE_ENCHANTMENTS, true);
            }

            set(4, submit, player -> {
                build.setSubmitted(!build.submitted());
                new BuildGUI(build, submitter, player, prevMenu);
            });
            setTeleportButton(5, location);
            set(6, createItem(ItemTypes.BARRIER, Text.of(MenuText.DELETE_NAME), MenuText.DELETE_DESC.stream().map(Text::of).toArray(Text[]::new)),
                    player -> {
                        submitter.removeBuild(build.getName());
                        player.closeInventory(generatePluginCause());
                        if (prevMenu != null) {
                            Task.builder().delayTicks(1).execute(prevMenu::open).submit(SpongeServerSaturday.instance());
                        }
                    });

            setFeatureButton(7);
        }
        else {
            setTeleportButton(0, location);
            set(1, createItem(ItemTypes.BOOK, Text.of(MenuText.DESCRIPTION_NAME)),
                    player -> {
                        BookView.Builder bvb = BookView.builder();
                        bvb.author(Text.of(submitter.getName()));
                        bvb.addPages(build.getDescription().stream().map(Text::of).collect(Collectors.toList()));
                        bvb.title(Text.of(build.getName()));
                        player.sendBookView(bvb.build());
                    });

            set(2, createItem(ItemTypes.PAINTING, Text.of(MenuText.RESOURCE_PACK_NAME), Text.of(build.getResourcePack())));
            setFeatureButton(3);
        }

        setBackButton(8, ItemTypes.ARROW);
    }

    private void setFeatureButton(int slot) {
        if (player.hasPermission(Permissions.FEATURE)) {
            ItemStack featured = createItem(ItemTypes.GOLDEN_APPLE, Text.of(MenuText.FEATURE_NAME), MenuText.FEATURE_DESC.stream().map(Text::of).toArray(Text[]::new));
            if (build.featured()) {
                featured.offer(Keys.GOLDEN_APPLE_TYPE, GoldenApples.ENCHANTED_GOLDEN_APPLE);
            }

            set(slot, featured, player -> {
                build.setFeatured(!build.featured());
                new BuildGUI(build, submitter, player, prevMenu);
            });
        }
    }

    private void setTeleportButton(int slot, Location<World> location) {
        set(slot, createItem(ItemTypes.COMPASS, Text.of(MenuText.TELEPORT_NAME), Stream.of(MenuText.teleportDesc(location.getExtent().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ())).map(Text::of).toArray(Text[]::new)),
                player -> {
                    if (player.hasPermission(Permissions.VIEW_GOTO)) {
                        player.setLocation(build.getLocation());
                        player.sendMessage(Text.builder(Messages.teleportedToBuild(build)).color(TextColors.GOLD).build());
                    }
                    else {
                        player.sendMessage(Text.builder(Messages.NO_PERMISSION).color(TextColors.RED).build());
                    }
                });
    }
}
