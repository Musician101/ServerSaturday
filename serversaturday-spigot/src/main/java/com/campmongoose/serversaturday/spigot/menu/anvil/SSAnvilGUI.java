package com.campmongoose.serversaturday.spigot.menu.anvil;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotChestMenu;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

public abstract class SSAnvilGUI extends AnvilGUI {

    @Nullable
    protected final AbstractSpigotChestMenu prevMenu;

    public SSAnvilGUI(@Nonnull Player player, @Nullable AbstractSpigotChestMenu prevMenu, BiFunction<Player, String, String> biFunction) {
        super(SpigotServerSaturday.instance(), player, MenuText.RENAME_ME, biFunction);
        this.prevMenu = prevMenu;
    }
}
