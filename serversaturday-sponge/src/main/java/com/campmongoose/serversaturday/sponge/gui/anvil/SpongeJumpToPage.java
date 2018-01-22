package com.campmongoose.serversaturday.sponge.gui.anvil;

import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

public class SpongeJumpToPage extends AnvilGUI {

    public SpongeJumpToPage(@Nonnull Player player, int maxPage, @Nonnull BiConsumer<Player, Integer> biConsumer) {
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

            SpongeServerSaturday.instance().ifPresent(plugin -> Task.builder().execute(() -> biConsumer.accept(player, page)).delayTicks(1L).submit(plugin));
            return null;
        });
    }
}
