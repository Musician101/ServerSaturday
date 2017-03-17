package com.campmongoose.serversaturday.spigot.gui.anvil;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.gui.chest.AbstractSpigotChestGUI;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

public abstract class SSAnvilGUI extends AnvilGUI {

    @Nullable
    protected final AbstractSpigotChestGUI prevMenu;

    public SSAnvilGUI(@Nonnull Player player, @Nullable AbstractSpigotChestGUI prevMenu, BiFunction<Player, String, String> biFunction) {
        super(SpigotServerSaturday.instance(), player, MenuText.RENAME_ME, biFunction);
        this.prevMenu = prevMenu;
    }
}
