package com.campmongoose.serversaturday.spigot.menu.anvil;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotChestMenu;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class SSAnvilGUI extends AnvilGUI {

    @Nonnull
    private final AbstractSpigotChestMenu prevMenu;

    public SSAnvilGUI(@Nonnull Player player, @Nonnull AbstractSpigotChestMenu prevMenu, BiFunction<Player, String, String> biFunction) {
        super(SpigotServerSaturday.instance(), player, MenuText.RENAME_ME, biFunction);
        this.prevMenu = prevMenu;
    }

    @Override
    public void closeInventory() {
        super.closeInventory();
        Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotServerSaturday.instance(), prevMenu::open);
    }
}
