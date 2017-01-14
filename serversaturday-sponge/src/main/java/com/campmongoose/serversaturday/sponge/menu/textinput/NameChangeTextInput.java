package com.campmongoose.serversaturday.sponge.menu.textinput;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.menu.chest.AbstractSpongeChestMenu;
import com.campmongoose.serversaturday.sponge.menu.chest.BuildMenu;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class NameChangeTextInput extends TextInput {

    private final SpongeBuild build;

    public NameChangeTextInput(SpongeBuild build, Player player, AbstractSpongeChestMenu prevMenu) {
        super(player, prevMenu);
        this.build = build;
    }

    @Override
    protected void build() {
        player.sendMessage(Text.builder(Messages.PREFIX + "Please type in chat what the new name. Type /cancel to go back.").color(TextColors.GOLD).build());
        biFunction = (rawMessage, player) -> {
            if (rawMessage.equalsIgnoreCase("/cancel")) {
                prevMenu.open();
                return null;
            }

            SpongeSubmitter submitter = SpongeServerSaturday.instance().getSubmissions().getSubmitter(player);
            if (submitter.getBuild(rawMessage) != null) {
                player.sendMessage(Text.builder(Messages.BUILD_ALREADY_EXISTS).color(TextColors.RED).build());
                return null;
            }

            submitter.updateBuildName(build, rawMessage);
            new BuildMenu(build, submitter, player, prevMenu);
            return rawMessage;
        };
    }
}
