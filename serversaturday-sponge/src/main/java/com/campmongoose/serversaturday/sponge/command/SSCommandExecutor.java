package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public abstract class SSCommandExecutor implements CommandExecutor {

    @Nonnull
    protected SpongeServerSaturday getPlugin() {
        return SpongeServerSaturday.instance();
    }

    @Nonnull
    protected SpongeSubmissions getSubmissions() {
        return getPlugin().getSubmissions();
    }

    @Nonnull
    protected SpongeSubmitter getSubmitter(@Nonnull Player player) {
        return getSubmissions().getSubmitter(player);
    }

    @Nonnull
    protected CommandResult playerOnly(CommandSource source) {
        source.sendMessage(Text.of(TextColors.RED, Messages.PLAYER_ONLY));
        return CommandResult.empty();
    }
}
