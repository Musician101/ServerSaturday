package com.campmongoose.serversaturday.sponge.gui.anvil.page;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.TriConsumer;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.gui.anvil.AnvilGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.AbstractSpongeChestGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.AllSubmissionsGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.SubmissionsGUI;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

public class JumpToPage extends AnvilGUI {

    public JumpToPage(@Nonnull Player player, @Nullable AbstractSpongeChestGUI prevMenu, int maxPage) {
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
            if (m == null) {
                messageStart = "A NULL menu";
            }
            else {
                messageStart = m.getClass().getName();
            }

            throw new UnsupportedOperationException(messageStart + " is not supported with this constructor. Please use JumpToPage(Player, AbstractSpongeChestGUI, Integer, TriConsumer)");
        });
    }

    public JumpToPage(@Nonnull Player player, @Nullable AbstractSpongeChestGUI prevMenu, int maxPage, TriConsumer<Player, Integer, AbstractSpongeChestGUI> triConsumer) {
        super(player, (p, name) -> {
            int page;
            try {
                page = Integer.parseInt(name);
            }
            catch (NumberFormatException e) {
                return MenuText.NOT_A_NUMBER;
            }

            if (page > maxPage) {
                return MenuText.maxNumber(maxPage);
            }

            Task.builder().delayTicks(1L).name("SS-JumpToPage-Task").execute(() -> triConsumer.accept(player, page, prevMenu)).submit(SpongeServerSaturday.instance());
            return null;
        });
    }
}
