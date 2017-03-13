package com.campmongoose.serversaturday.spigot.menu.anvil.page;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.TriConsumer;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotChestMenu;
import com.campmongoose.serversaturday.spigot.menu.anvil.SSAnvilGUI;
import com.campmongoose.serversaturday.spigot.menu.chest.AllSubmissionsMenu;
import com.campmongoose.serversaturday.spigot.menu.chest.SubmissionsMenu;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class JumpToPage extends SSAnvilGUI {

    public JumpToPage(@Nonnull Player player, @Nonnull AbstractSpigotChestMenu prevMenu, int maxPage) {
        this(player, prevMenu, maxPage, (p, pg, m) -> {
            if (prevMenu instanceof AllSubmissionsMenu) {
                new AllSubmissionsMenu(p, pg, m);
            }
            else if (prevMenu instanceof SubmissionsMenu) {
                new SubmissionsMenu(p, pg, m);
            }

            throw new UnsupportedOperationException(prevMenu.getClass().getName() + " is not supported with this constructor. " +
                    "Please use new JumpToPage(Player, AbstractSpigotChestMenu, TriConsumer)");
        });
    }

    public JumpToPage(@Nonnull Player player, @Nonnull AbstractSpigotChestMenu prevMenu, int maxPage, @Nonnull TriConsumer<Player, Integer, AbstractSpigotChestMenu> biConsumer) {
        super(player, prevMenu, (p, name) -> {
            int page;
            try {
                page = Integer.parseInt(name);
            }
            catch (NumberFormatException e) {
                return MenuText.NOT_A_NUMBER;
            }

            if (page > maxPage) {
                return Integer.toString(maxPage);
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotServerSaturday.instance(), () -> biConsumer.accept(player, page, prevMenu));
            return null;
        });
    }
}
