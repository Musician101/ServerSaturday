package com.campmongoose.serversaturday.spigot.gui.anvil;

import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SpigotJumpToPage extends SSAnvilGUI {

    public SpigotJumpToPage(@Nonnull Player player, int maxPage, @Nonnull BiConsumer<Player, Integer> biConsumer) {
        super(player, (p, name) -> {
            int page;
            try {
                page = Integer.parseInt(name);
            }
            catch (NumberFormatException e) {
                return "That is not a number!";
            }

            if (page > maxPage) {
                return "Page cannot exceed " + maxPage;
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask((SpigotServerSaturday) SpigotServerSaturday.instance(), () -> biConsumer.accept(player, page));
            return null;
        });
    }
}
