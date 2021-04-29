package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.google.common.collect.ImmutableMap;
import io.musician101.musicianlibrary.java.minecraft.sponge.SpongeTextInput;
import io.musician101.musicianlibrary.java.minecraft.sponge.gui.chest.SpongeIconBuilder;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.menu.ClickTypes;
import org.spongepowered.api.world.server.ServerLocation;

public class SubmitterGUI extends SpongeServerSaturdayChestGUI {

    private int page = 1;

    public SubmitterGUI(@Nonnull Submitter<Component> submitter, @Nonnull ServerPlayer player) {
        super(player, Component.text(MenuText.submitterMenu(submitter)), 54);
        updateSlots(submitter);
        if (player.uniqueId().equals(submitter.getUUID())) {
            setButton(51, SpongeIconBuilder.of(ItemTypes.EMERALD_BLOCK, Component.text(MenuText.NEW_BUILD)), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> {

            }));
        }
        setButton(50, SpongeIconBuilder.of(ItemTypes.BARRIER, Component.text("Back")), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), SubmittersGUI::new));
    }

    private void updateSlots(@Nonnull Submitter<Component> submitter) {
        List<Build<Component>> builds = submitter.getBuilds();
        builds.sort(Comparator.comparing(Build::getName));
        IntStream.of(0, 45).forEach(x -> {
            try {
                int index = x + (page - 1) * 45;
                Build<Component> build = builds.get(index);
                ItemStack itemStack = SpongeIconBuilder.builder(ItemTypes.BOOK).name(Component.text(submitter.getName()).color(build.submitted() && !build.featured() ? NamedTextColor.RED : NamedTextColor.GREEN)).build();
                setButton(x, itemStack, ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> {
                    if (submitter.getUUID().equals(p.uniqueId())) {
                        new EditBuildGUI(build, submitter, p);
                        return;
                    }

                    new ViewBuildGUI(build, submitter, p);
                }, ClickTypes.CLICK_RIGHT.get(), p -> {
                    if (submitter.getUUID().equals(p.uniqueId())) {
                        builds.remove(index);
                        updateSlots(submitter);
                    }
                }));
            }
            catch (IndexOutOfBoundsException ignored) {

            }
        });

        if (player.uniqueId().equals(submitter.getUUID()) && (builds.size() <= SpongeServerSaturday.instance().getConfig().getMaxBuilds() || player.hasPermission(Permissions.EXCEED_MAX_BUILDS))) {
            setButton(48, SpongeIconBuilder.of(ItemTypes.EMERALD_BLOCK, Component.text(MenuText.NEW_BUILD)), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> {
                p.closeInventory();
                p.sendMessage(Component.text(Messages.SET_BUILD_NAME, NamedTextColor.GREEN));
                new SpongeTextInput(SpongeServerSaturday.instance().getPluginContainer(), p) {

                    @Override
                    protected void process(ServerPlayer player, String s) {
                        if (submitter.getBuild(s) != null) {
                            player.sendMessage(Component.text(Messages.BUILD_ALREADY_EXISTS, NamedTextColor.RED));
                            return;
                        }

                        ServerLocation location = (ServerLocation) player.location();
                        Build<Component> build = submitter.newBuild(s, new io.musician101.musicianlibrary.java.minecraft.common.Location(location.worldKey().asString(), location.x(), location.y(), location.z()));
                        new EditBuildGUI(build, submitter, player);
                    }
                };
            }));

            setButton(50, SpongeIconBuilder.of(ItemTypes.BARRIER, Component.text("Back")), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), SubmittersGUI::new));
        }
        else {
            removeButton(48);
            removeButton(50);
            setButton(49, SpongeIconBuilder.of(ItemTypes.BARRIER, Component.text("Back")), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), SubmittersGUI::new));
        }

        if (page == 1) {
            removeButton(45);
        }
        else {
            setButton(45, SpongeIconBuilder.of(ItemTypes.ARROW, Component.text(MenuText.PREVIOUS_PAGE)), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> {
                page--;
                updateSlots(submitter);
            }));
        }

        int maxPage = Double.valueOf(Math.ceil(builds.size() / 45d)).intValue();
        if (page < maxPage) {
            removeButton(53);
        }
        else {
            setButton(53, SpongeIconBuilder.of(ItemTypes.ARROW, Component.text(MenuText.NEXT_PAGE)), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> {
                page++;
                updateSlots(submitter);
            }));
        }
    }
}
