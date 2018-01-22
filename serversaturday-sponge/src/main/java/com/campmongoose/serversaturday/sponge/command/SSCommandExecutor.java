package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.common.submission.Submissions;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public abstract class SSCommandExecutor implements CommandExecutor {

    @Nonnull
    protected Optional<SpongeServerSaturday> getPluginInstance() {
        return SpongeServerSaturday.instance();
    }

    @Nonnull
    protected Optional<SpongeSubmissions> getSubmissions() {
        return getPluginInstance().map(ServerSaturday::getSubmissions);
    }

    @Nonnull
    protected Optional<SpongeSubmitter> getSubmitter(@Nonnull Player player) {
        return getSubmissions().map(submissions -> submissions.getSubmitter(player));
    }

    @Nonnull
    protected Optional<SpongeSubmitter> getSubmitter(@Nonnull String playerName) {
        return Sponge.getServiceManager().getRegistration(UserStorageService.class).map(userStorage -> userStorage.getProvider().get(playerName).map(User::getUniqueId).flatMap(this::getSubmitter)).orElseGet(() -> getSubmissions().map(Submissions::getSubmitters).map(List::stream).flatMap(stream -> stream.filter(s -> playerName.equalsIgnoreCase(s.getName())).findFirst()));
    }

    @Nonnull
    protected Optional<SpongeSubmitter> getSubmitter(@Nonnull UUID uuid) {
        return getSubmissions().map(submissions -> submissions.getSubmitter(uuid));
    }

    @Nonnull
    protected CommandResult playerOnly(CommandSource source) {
        source.sendMessage(Text.builder(Messages.PLAYER_ONLY).color(TextColors.RED).build());
        return CommandResult.empty();
    }
}
