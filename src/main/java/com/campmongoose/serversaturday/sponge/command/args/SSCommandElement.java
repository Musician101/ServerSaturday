package com.campmongoose.serversaturday.sponge.command.args;

import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.common.submission.Submissions;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

public abstract class SSCommandElement extends CommandElement {

    public SSCommandElement(@Nullable Text key) {
        super(key);
    }

    @Nonnull
    protected String getBuild(@Nonnull CommandArgs args) throws ArgumentParseException {
        StringBuilder sb = new StringBuilder(args.next());
        while (args.hasNext()) {
            sb.append(args.next());
        }

        return sb.toString();
    }

    @Nonnull
    protected Optional<SpongeSubmissions> getSubmissions() {
        return SpongeServerSaturday.instance().map(ServerSaturday::getSubmissions);
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
    protected Optional<SpongeSubmitter> getSubmitter(@Nonnull Player player) {
        return getSubmissions().map(submissions -> submissions.getSubmitter(player));
    }
}
