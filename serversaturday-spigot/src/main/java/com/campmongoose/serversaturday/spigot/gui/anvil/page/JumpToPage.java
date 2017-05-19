package com.campmongoose.serversaturday.spigot.gui.anvil.page;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.TriConsumer;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.gui.anvil.SSAnvilGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.AbstractSpigotChestGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.AllSubmissionsGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.SubmissionsGUI;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class JumpToPage extends SSAnvilGUI {

    public JumpToPage(@Nonnull Player player, @Nullable AbstractSpigotChestGUI prevMenu, int maxPage) {
        this(player, prevMenu, maxPage, (p, pg, m) -> {
            if (p == null) {
                throw new NullPointerException("Tried to open a menu without a registered player!");
            }

            if (pg == null) {
                throw new NullPointerException("Tried to accept an input that was NULL!");
            }

            if (prevMenu instanceof AllSubmissionsGUI) {
                new AllSubmissionsGUI(p, pg, m);
            }
            else if (prevMenu instanceof SubmissionsGUI) {
                new SubmissionsGUI(p, pg, m);
            }

            String messageStart;
            if (prevMenu == null) {
                messageStart = "A NULL menu";
            }
            else {
                messageStart = prevMenu.getClass().getName();
            }

            throw new UnsupportedOperationException(messageStart + " is not supported with this constructor. " +
                    "Please use new JumpToPage(Player, AbstractSpigotChestGUI, TriConsumer)");
        });
    }

    public JumpToPage(@Nonnull Player player, @Nullable AbstractSpigotChestGUI prevMenu, int maxPage, @Nonnull TriConsumer<Player, Integer, AbstractSpigotChestGUI> biConsumer) {
        super(player, (p, name) -> {
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
