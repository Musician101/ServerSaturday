package com.campmongoose.serversaturday.sponge.gui.chest.build;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.sponge.gui.chest.AbstractSpongeChestGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.BookView;
import org.spongepowered.api.text.Text;

public class ViewBuildGUI extends BuildGUI {

    public ViewBuildGUI(@Nonnull SpongeBuild build, @Nonnull SpongeSubmitter submitter, Player player, @Nullable AbstractSpongeChestGUI prevMenu) {
        super(build, submitter, player, prevMenu, true);
        if (!submitter.getUUID().equals(player.getUniqueId())) {
            open();
        }
    }

    @Override
    protected void build() {
        setTeleportButton(0, player.getLocation());
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
        setBackButton(8, ItemTypes.ARROW);
    }
}
