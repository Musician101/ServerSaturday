package com.campmongoose.serversaturday.sponge.gui.chest.build;

import com.campmongoose.serversaturday.sponge.gui.chest.AbstractSpongeChestGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.entity.living.player.Player;

public class EditBuildGUI extends BuildGUI {

    public EditBuildGUI(@Nonnull SpongeBuild build, @Nonnull SpongeSubmitter submitter, Player player, @Nullable AbstractSpongeChestGUI prevMenu) {
        super(build, submitter, player, prevMenu);
    }
}
