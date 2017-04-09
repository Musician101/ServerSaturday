package com.campmongoose.serversaturday.sponge.gui.textinput;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.gui.chest.AbstractSpongeChestGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.BuildGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class NameChangeTextInput extends TextInput {

    private final SpongeBuild build;

    public NameChangeTextInput(SpongeBuild build, Player player, AbstractSpongeChestGUI prevMenu) {
        super(player, prevMenu, (rawMessage, p) -> {
            if (rawMessage.equalsIgnoreCase("/cancel")) {
                if (prevMenu != null) {
                    prevMenu.open();
                }

                return null;
            }

            SpongeSubmitter submitter = SpongeServerSaturday.instance().getSubmissions().getSubmitter(p);
            if (submitter.getBuild(rawMessage) != null) {
                p.sendMessage(Text.builder(Messages.BUILD_ALREADY_EXISTS).color(TextColors.RED).build());
                return null;
            }

            submitter.updateBuildName(build, rawMessage);
            new BuildGUI(build, submitter, p, prevMenu);
            return rawMessage;
        });
        player.sendMessage(Text.builder(Messages.PREFIX + "Please type in chat what the new name. Type /cancel to go back.").color(TextColors.GOLD).build());
        this.build = build;
    }
}
