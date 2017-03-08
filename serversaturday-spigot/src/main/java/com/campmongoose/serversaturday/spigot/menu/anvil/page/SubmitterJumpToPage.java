package com.campmongoose.serversaturday.spigot.menu.anvil.page;

import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotChestMenu;
import com.campmongoose.serversaturday.spigot.menu.chest.SubmitterMenu;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import javax.annotation.Nonnull;
import org.bukkit.entity.Player;

public class SubmitterJumpToPage extends JumpToPage {

    public SubmitterJumpToPage(@Nonnull Player player, @Nonnull AbstractSpigotChestMenu prevMenu, int maxPage, @Nonnull SpigotSubmitter submitter) {
        super(player, prevMenu, maxPage, (p, pg, m) -> new SubmitterMenu(p, submitter, pg, m));
    }
}
