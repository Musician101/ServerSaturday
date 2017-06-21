package com.campmongoose.serversaturday.sponge.gui.anvil.page;

import com.campmongoose.serversaturday.sponge.gui.chest.AbstractSpongeChestGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.SubmitterGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.entity.living.player.Player;

public class SubmitterJumpToPage extends JumpToPage {

    public SubmitterJumpToPage(@Nonnull Player player, @Nullable AbstractSpongeChestGUI prevMenu, int maxPage, @Nonnull SpongeSubmitter submitter) {
        super(player, prevMenu, maxPage, (p, pg, m) -> {
            if (p == null) {
                throw new NullPointerException("Tried to open a menu without a registered player!");
            }

            if (pg == null) {
                throw new NullPointerException("Tried to accept an input that was NULL!");
            }

            new SubmitterGUI(p, submitter, pg, m);
        });
    }
}
