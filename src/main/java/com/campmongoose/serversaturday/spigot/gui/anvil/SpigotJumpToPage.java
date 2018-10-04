package com.campmongoose.serversaturday.spigot.gui.anvil;

import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SpigotJumpToPage extends SSAnvilGUI {

    public SpigotJumpToPage(@Nonnull Player player, int maxPage, @Nonnull Consumer<Integer> consumer) {
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

            Bukkit.getScheduler().scheduleSyncDelayedTask((SpigotServerSaturday) SpigotServerSaturday.instance(), () -> consumer.accept(page));
            return null;
        });
    }
}
