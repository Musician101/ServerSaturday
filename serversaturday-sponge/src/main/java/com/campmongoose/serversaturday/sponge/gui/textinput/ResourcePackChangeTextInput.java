package com.campmongoose.serversaturday.sponge.gui.textinput;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.gui.chest.AbstractSpongeChestGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.BuildGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import javax.annotation.Nonnull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ResourcePackChangeTextInput extends TextInput {

    @Nonnull
    private final SpongeBuild build;

    public ResourcePackChangeTextInput(@Nonnull SpongeBuild build, @Nonnull Player player, @Nonnull AbstractSpongeChestGUI prevMenu) {
        super(player, prevMenu);
        this.build = build;
    }

    @Override
    protected void build() {
        player.sendMessage(Text.builder(Messages.PREFIX + "Please type the name of the resource pack to be used when viewing this build.").color(TextColors.RED).build());
        biFunction = (rawMessage, player) -> {
            if (rawMessage.equalsIgnoreCase("/cancel")) {
                if (prevMenu != null)
                    prevMenu.open();

                return null;
            }

            SpongeSubmitter submitter = SpongeServerSaturday.instance().getSubmissions().getSubmitter(player);
            submitter.updateBuildResourcePack(build, rawMessage);
            new BuildGUI(submitter.getBuild(build.getName()), submitter, player, prevMenu);
            return rawMessage;
        };
    }
}
