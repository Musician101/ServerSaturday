package com.campmongoose.serversaturday.spigot.menu.anvil;

import com.campmongoose.serversaturday.spigot.menu.chest.AbstractSpigotChestMenu;
import com.campmongoose.serversaturday.spigot.menu.chest.BuildMenu;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.entity.Player;

public class NameChangeMenu extends SSAnvilGUI {

    public NameChangeMenu(@Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter, @Nonnull Player player, @Nullable AbstractSpigotChestMenu prevMenu) {
        super(player, prevMenu, (p, name) ->
        {
            submitter.updateBuildName(build, name);
            SpigotBuild spigotBuild = submitter.getBuild(name);
            if (spigotBuild != null) {
                new BuildMenu(spigotBuild, submitter, player, prevMenu);
            }

            return null;
        });
    }
}
