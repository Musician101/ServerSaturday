package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.sponge.gui.SpongeBookGUI;
import com.campmongoose.serversaturday.sponge.gui.SpongeIconBuilder;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import com.google.common.collect.ImmutableMap;
import javax.annotation.Nonnull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;

public class ViewBuildGUI extends BuildGUI {

    public ViewBuildGUI(@Nonnull SpongeBuild build, @Nonnull SpongeSubmitter submitter, @Nonnull Player player) {
        super(build, 3, 0, player);
        setButton(1, SpongeIconBuilder.builder(ItemTypes.BOOK).name(Text.of(MenuText.DESCRIPTION_NAME)).description(Text.of(MenuText.DESCRIPTION_DESC)).build(), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> SpongeBookGUI.openWrittenBook(p, build, submitter)));
        setButton(2, SpongeIconBuilder.builder(ItemTypes.PAINTING).name(Text.of(MenuText.RESOURCE_PACK_NAME)).description(Text.of(MenuText.RESOURCE_PACK_DESC)).build(), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> SpongeBookGUI.openWrittenBook(p, build, submitter)));
        setButton(8, SpongeIconBuilder.of(ItemTypes.BARRIER, Text.of("Back")), ImmutableMap.of(ClickInventoryEvent.Primary.class, p -> new SubmitterGUI(submitter, p)));
    }
}
