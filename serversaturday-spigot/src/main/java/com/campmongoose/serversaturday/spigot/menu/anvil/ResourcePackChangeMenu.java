package com.campmongoose.serversaturday.spigot.menu.anvil;

import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotChestMenu;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.entity.Player;

public class ResourcePackChangeMenu extends SSAnvilGUI {

    public ResourcePackChangeMenu(@Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter, @Nonnull Player player, @Nullable AbstractSpigotChestMenu prevMenu) {
        super(player, prevMenu, (p, name) ->
        {
            submitter.updateBuildResourcePack(build, name);
            return null;
        });
    }
}
