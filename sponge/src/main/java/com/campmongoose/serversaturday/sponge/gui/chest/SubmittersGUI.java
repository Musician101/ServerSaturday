package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.google.common.collect.ImmutableMap;
import io.musician101.musicianlibrary.java.minecraft.sponge.gui.chest.SpongeIconBuilder;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.menu.ClickTypes;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.GameProfileManager;

public class SubmittersGUI extends SpongeServerSaturdayChestGUI {

    private int page = 1;

    public SubmittersGUI(@Nonnull ServerPlayer player) {
        super(player, Component.text(MenuText.SUBMISSIONS), 54);
        updateSlots();
        setButton(50, SpongeIconBuilder.of(ItemTypes.BARRIER, Component.text("Back")), ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), SubmittersGUI::new));
    }

    private void updateSlots() {
        List<Submitter<Component>> submitters = SpongeServerSaturday.instance().getSubmissions().getData();
        submitters.sort(Comparator.comparing(Submitter::getName));
        IntStream.of(0, 45).forEach(x -> {
            try {
                int index = x + (page - 1) * 45;
                Submitter<Component> submitter = submitters.get(index);
                GameProfileManager gpm = Sponge.server().gameProfileManager();
                GameProfile gameProfile = gpm.cache().findById(submitter.getUUID()).orElse(GameProfile.of(submitter.getUUID(), submitter.getName()));
                boolean hasNonFeaturedBuilds = submitter.getBuilds().stream().anyMatch(build -> build.submitted() && !build.featured());
                ItemStack icon =  SpongeIconBuilder.builder(ItemTypes.PLAYER_HEAD).offer(Keys.GAME_PROFILE, gameProfile).offer(Keys.DISPLAY_NAME, Component.text(gameProfile.name().orElse(gameProfile.name().get())).color(hasNonFeaturedBuilds ? NamedTextColor.GREEN : NamedTextColor.RED)).build();
                setButton(x, icon, ImmutableMap.of(ClickTypes.CLICK_LEFT.get(), p -> new SubmitterGUI(submitter, p)));
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

        int maxPage = Double.valueOf(Math.ceil(submitters.size() / 45d)).intValue();
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
