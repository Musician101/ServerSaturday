package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import io.musician101.musicianlibrary.java.minecraft.sponge.gui.chest.SpongeIconBuilder;
import java.util.ArrayList;
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

public class AllSubmissionsGUI extends SpongeServerSaturdayChestGUI {

    private int page = 1;

    public AllSubmissionsGUI(@Nonnull ServerPlayer player) {
        super(player, Component.text(MenuText.ALL_SUBMISSIONS), 54);
        updateSlots();
        setButton(50, SpongeIconBuilder.of(ItemTypes.BARRIER, Component.text("Back")), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), ServerPlayer::closeInventory));
    }

    private void updateSlots() {
        Multimap<Build<Component>, Submitter<Component>> map = TreeMultimap.create(Comparator.comparing(Build::getName), Comparator.comparing(Submitter::getName));
        SpongeServerSaturday.instance().getSubmissions().getData().forEach(submitter -> submitter.getBuilds().stream().filter(build -> !build.featured() && build.submitted()).forEach(build -> map.put(build, submitter)));
        List<Build<Component>> builds = new ArrayList<>(map.keys());
        IntStream.of(0, 45).forEach(x -> {
            try {
                int index = x + (page - 1) * 45;
                Build<Component> build = builds.get(index);
                Submitter<Component> submitter = map.get(build).iterator().next();
                map.remove(build, submitter);
                ItemStack itemStack = SpongeIconBuilder.builder(ItemTypes.BOOK).name(Component.text(submitter.getName(), build.submitted() && !build.featured() ? NamedTextColor.GREEN : NamedTextColor.RED)).build();
                setButton(x, itemStack, ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> {
                    if (p.uniqueId().equals(submitter.getUUID())) {
                        new EditBuildGUI(build, submitter, p);
                        return;
                    }

                    new ViewBuildGUI(build, submitter, p);
                }));
            }
            catch (IndexOutOfBoundsException ignored) {

            }
        });

        if (page == 1) {
            removeButton(45);
        }
        else {
            setButton(45, SpongeIconBuilder.of(ItemTypes.ARROW, Component.text(MenuText.PREVIOUS_PAGE)), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> {
                page--;
                updateSlots();
            }));
        }

        int maxPage = Double.valueOf(Math.ceil(map.size() / 45d)).intValue();
        if (page < maxPage) {
            removeButton(53);
        }
        else {
            setButton(53, SpongeIconBuilder.of(ItemTypes.ARROW, Component.text(MenuText.NEXT_PAGE)), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> {
                page++;
                updateSlots();
            }));
        }
    }
}
