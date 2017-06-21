package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.command.AbstractCommand;
import com.campmongoose.serversaturday.common.command.SSCommandException;
import com.campmongoose.serversaturday.common.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.service.ProviderRegistration;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public abstract class AbstractSpongeCommand extends AbstractCommand<SpongeBuild, SpongeServerSaturday, Location<World>, Player, SpongeSubmitter, SpongeSubmissions, ItemStack> implements CommandExecutor {

    @Nonnull
    @Override
    protected SpongeServerSaturday getPluginInstance() {
        return SpongeServerSaturday.instance();
    }

    @Nonnull
    @Override
    protected SpongeSubmissions getSubmissions() throws SSCommandException {
        try {
            return getPluginInstance().getSubmissions();
        }
        catch (SubmissionsNotLoadedException e) {
            throw new SSCommandException(e.getMessage());
        }
    }

    @Nonnull
    @Override
    protected SpongeSubmitter getSubmitter(@Nonnull Player player) throws SSCommandException {
        return getSubmissions().getSubmitter(player);
    }

    @Nullable
    @Override
    protected SpongeSubmitter getSubmitter(@Nonnull String playerName) throws SSCommandException {
        Optional<ProviderRegistration<UserStorageService>> providerRegistration = Sponge.getServiceManager().getRegistration(UserStorageService.class);
        if (providerRegistration.isPresent()) {
            UserStorageService userStorage = providerRegistration.get().getProvider();
            Optional<User> user = userStorage.get(playerName);
            if (user.isPresent()) {
                return getSubmitter(user.get().getUniqueId());
            }
            else {
                for (SpongeSubmitter s : getSubmissions().getSubmitters()) {
                    if (s.getName().equalsIgnoreCase(playerName)) {
                        return s;
                    }
                }
            }
        }

        return null;
    }

    @Nullable
    @Override
    protected SpongeSubmitter getSubmitter(@Nonnull UUID uuid) throws SSCommandException {
        return getSubmissions().getSubmitter(uuid);
    }

    @Nonnull
    protected CommandResult playerOnly(CommandSource source) {
        source.sendMessage(Text.builder(Messages.PLAYER_ONLY).color(TextColors.RED).build());
        return CommandResult.empty();
    }
}
