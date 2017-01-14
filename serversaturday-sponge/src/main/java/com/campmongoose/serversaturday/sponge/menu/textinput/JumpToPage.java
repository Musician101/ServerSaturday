package com.campmongoose.serversaturday.sponge.menu.textinput;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.menu.chest.AbstractSpongeChestMenu;
import com.campmongoose.serversaturday.sponge.menu.chest.AllSubmissionsMenu;
import com.campmongoose.serversaturday.sponge.menu.chest.SubmissionsMenu;
import com.campmongoose.serversaturday.sponge.menu.chest.SubmitterMenu;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class JumpToPage extends TextInput {

    public JumpToPage(Player player, AbstractSpongeChestMenu prevMenu) {
        super(player, prevMenu);
    }

    @Override
    protected void build() {
        player.sendMessage(Text.builder(Messages.PREFIX + "Please type in chat what page you would like to jump to. Type /cancel to go back.").color(TextColors.GOLD).build());
        biFunction = (rawMessage, player) -> {
            if (rawMessage.equalsIgnoreCase("/cancel")) {
                prevMenu.open();
                return null;
            }

            int page;
            try {
                page = Integer.parseInt(rawMessage);
            }
            catch (NumberFormatException e) {
                player.sendMessage(Text.of(Messages.PREFIX + "The message"));
                return null;
            }

            if (prevMenu instanceof AllSubmissionsMenu) {
                new AllSubmissionsMenu(player, page, prevMenu);
            }
            else if (prevMenu instanceof SubmissionsMenu) {
                new SubmissionsMenu(player, page, prevMenu);
            }
            else if (prevMenu instanceof SubmitterMenu) {
                new SubmitterMenu(player, SpongeServerSaturday.instance().getSubmissions().getSubmitter(player), page, prevMenu);
            }
            else {
                return null;
            }

            return rawMessage;
        };
    }
}
