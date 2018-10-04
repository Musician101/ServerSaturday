package com.campmongoose.serversaturday.spigot.gui.anvil;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

public class SSAnvilGUI extends AnvilGUI {

    public SSAnvilGUI(@Nonnull Player player, @Nonnull BiFunction<Player, String, String> biFunction) {
        this(player, MenuText.RENAME_ME, biFunction);
    }

    public SSAnvilGUI(@Nonnull Player player, @Nonnull String name, @Nonnull BiFunction<Player, String, String> biFunction) {
        super((SpigotServerSaturday) SpigotServerSaturday.instance(), player, name, biFunction);
    }
}
