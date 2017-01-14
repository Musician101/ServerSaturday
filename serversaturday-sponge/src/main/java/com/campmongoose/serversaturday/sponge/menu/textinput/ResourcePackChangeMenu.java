package com.campmongoose.serversaturday.sponge.menu.textinput;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.menu.chest.AbstractSpongeChestMenu;
import com.campmongoose.serversaturday.sponge.menu.chest.BuildMenu;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import javax.annotation.Nonnull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ResourcePackChangeMenu extends TextInput {

    @Nonnull
    private final SpongeBuild build;

    public ResourcePackChangeMenu(@Nonnull SpongeBuild build, @Nonnull Player player, @Nonnull AbstractSpongeChestMenu prevMenu) {
        super(player, prevMenu);
        this.build = build;
    }

    @Override
    protected void build() {
        player.sendMessage(Text.builder(Messages.PREFIX + "Please type the name of the resource pack to be used when viewing this build.").color(TextColors.RED).build());
        biFunction = (rawMessage, player) -> {
            if (rawMessage.equalsIgnoreCase("/cancel")) {
                prevMenu.open();
                return null;
            }

            SpongeSubmitter submitter = SpongeServerSaturday.instance().getSubmissions().getSubmitter(player);
            submitter.updateBuildResourcePack(build, rawMessage);
            new BuildMenu(submitter.getBuild(build.getName()), submitter, player, prevMenu);
            return rawMessage;
        };
    }
}
