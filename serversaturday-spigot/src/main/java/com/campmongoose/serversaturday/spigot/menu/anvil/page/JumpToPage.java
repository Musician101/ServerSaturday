package com.campmongoose.serversaturday.spigot.menu.anvil.page;

import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotChestMenu;
import com.campmongoose.serversaturday.spigot.menu.anvil.SSAnvilGUI;
import com.campmongoose.serversaturday.spigot.menu.chest.AllSubmissionsMenu;
import com.campmongoose.serversaturday.spigot.menu.chest.SubmissionsMenu;
import com.campmongoose.serversaturday.spigot.menu.chest.SubmitterMenu;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.google.common.base.Preconditions.checkNotNull;

public class JumpToPage extends SSAnvilGUI {

    public JumpToPage(@Nonnull Player player, @Nonnull AbstractSpigotChestMenu prevMenu, @Nullable SpigotSubmitter submitter) {
        super(player, prevMenu, (p, name) -> {
            int page;
            try {
                page = Integer.parseInt(name);
            }
            catch (NumberFormatException e) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotServerSaturday.instance(), () -> new JumpToPage(player, prevMenu, submitter));
                return null;
            }

            if (prevMenu instanceof AllSubmissionsMenu) {
                new AllSubmissionsMenu(player, page, prevMenu);
            }
            else if (prevMenu instanceof SubmissionsMenu) {
                new SubmissionsMenu(player, page, prevMenu);
            }
            else if (prevMenu instanceof SubmitterMenu) {
                new SubmitterMenu(player, checkNotNull(submitter), page, prevMenu);
            }

            return name;
        });
    }
}
