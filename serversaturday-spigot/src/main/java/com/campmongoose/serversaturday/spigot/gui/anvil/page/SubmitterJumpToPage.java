package com.campmongoose.serversaturday.spigot.gui.anvil.page;

import com.campmongoose.serversaturday.spigot.gui.chest.AbstractSpigotChestGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.SubmitterGUI;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.entity.Player;

public class SubmitterJumpToPage extends JumpToPage {

    public SubmitterJumpToPage(@Nonnull Player player, @Nullable AbstractSpigotChestGUI prevMenu, int maxPage, @Nonnull SpigotSubmitter submitter) {
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
