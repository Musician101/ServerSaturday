package com.campmongoose.serversaturday.sponge.command.args;

import com.campmongoose.serversaturday.common.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
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
    protected SpongeSubmissions getSubmissions() throws SubmissionsNotLoadedException {
        return SpongeServerSaturday.instance().getSubmissions();
    }

    @Nonnull
    protected SpongeSubmitter getSubmitter(@Nonnull Player player) throws SubmissionsNotLoadedException {
        return getSubmissions().getSubmitter(player);
    }

    @Nullable
    protected SpongeSubmitter getSubmitter(@Nonnull String playerName) throws SubmissionsNotLoadedException {
        int nameLength = playerName.length();
        UserStorageService userStorage = Sponge.getServiceManager().provideUnchecked(UserStorageService.class);
        if (nameLength >= 3 && nameLength <= 16) {
            Optional<User> user = userStorage.get(playerName);
            if (user.isPresent()) {
                return getSubmitter(user.get().getUniqueId());
            }
        }

        if (nameLength <= 16) {
            for (SpongeSubmitter s : getSubmissions().getSubmitters()) {
                if (s.getName().equalsIgnoreCase(playerName)) {
                    return s;
                }
            }
        }

        return null;
    }

    @Nullable
    protected SpongeSubmitter getSubmitter(@Nonnull UUID uuid) throws SubmissionsNotLoadedException {
        return getSubmissions().getSubmitter(uuid);
    }

    @Nonnull
    protected String getBuild(@Nonnull CommandArgs args) throws ArgumentParseException {
        StringBuilder sb = new StringBuilder(args.next());
        while (args.hasNext()) {
            sb.append(args.next());
        }

        return sb.toString();
    }
}
