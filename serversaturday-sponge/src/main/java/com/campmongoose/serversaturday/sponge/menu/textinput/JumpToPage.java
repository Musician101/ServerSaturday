package com.campmongoose.serversaturday.sponge.menu.textinput;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.menu.chest.AbstractSpongeChestGUI;
import com.campmongoose.serversaturday.sponge.menu.chest.AllSubmissionsGUI;
import com.campmongoose.serversaturday.sponge.menu.chest.SubmissionsGUI;
import com.campmongoose.serversaturday.sponge.menu.chest.SubmitterGUI;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class JumpToPage extends TextInput {

    protected final int maxPage;

    public JumpToPage(@Nonnull Player player, @Nullable AbstractSpongeChestGUI prevMenu, int maxPage) {
        super(player, prevMenu);
        this.maxPage = maxPage;
    }

    @Override
    protected void build() {
        player.sendMessage(Text.builder(Messages.PREFIX + "Please type in chat what page you would like to jump to. Type /cancel to go back.").color(TextColors.GOLD).build());
        biFunction = (rawMessage, player) -> {
            if (rawMessage.equalsIgnoreCase("/cancel")) {
                if (prevMenu != null) {
                    prevMenu.open();
                }

                return null;
            }

            int page;
            try {
                page = Integer.parseInt(rawMessage);
            }
            catch (NumberFormatException e) {
                player.sendMessage(Text.of(Messages.PREFIX + "That is not a number."));
                return null;
            }

            if (prevMenu instanceof AllSubmissionsGUI) {
                new AllSubmissionsGUI(player, page, prevMenu);
            }
            else if (prevMenu instanceof SubmissionsGUI) {
                new SubmissionsGUI(player, page, prevMenu);
            }
            else if (prevMenu instanceof SubmitterGUI) {
                new SubmitterGUI(player, SpongeServerSaturday.instance().getSubmissions().getSubmitter(player), page, prevMenu);
            }
            else {
                return null;
            }

            return rawMessage;
        };
    }
}
