package com.campmongoose.serversaturday.spigot.menu.anvil;

import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotChestMenu;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import javax.annotation.Nonnull;
import org.bukkit.entity.Player;

public class NameChangeMenu extends SSAnvilGUI {

    public NameChangeMenu(@Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter, @Nonnull Player player, @Nonnull AbstractSpigotChestMenu prevMenu) {
        super(player, prevMenu, (p, name) ->
        {
            submitter.updateBuildName(build, name);
            return name;
        });
    }
}
