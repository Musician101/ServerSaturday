package com.campmongoose.serversaturday.spigot.gui.anvil;

import com.campmongoose.serversaturday.spigot.gui.chest.AbstractSpigotChestGUI;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.entity.Player;

public class ResourcePackChangeMenu extends SSAnvilGUI {

    public ResourcePackChangeMenu(@Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter, @Nonnull Player player, @Nullable AbstractSpigotChestGUI prevMenu) {
        super(player, prevMenu, (p, name) ->
        {
            submitter.updateBuildResourcePack(build, name);
            return null;
        });
    }
}
