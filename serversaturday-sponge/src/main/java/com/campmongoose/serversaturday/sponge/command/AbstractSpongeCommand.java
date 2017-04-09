package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.command.AbstractCommand;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public abstract class AbstractSpongeCommand extends AbstractCommand<SpongeBuild, SpongeServerSaturday, Location<World>, Player, SpongeSubmitter, SpongeSubmissions, ItemStack> implements CommandExecutor {

    @Nonnull
    protected SpongeServerSaturday getPluginInstance() {
        return SpongeServerSaturday.instance();
    }

    @Nonnull
    protected SpongeSubmissions getSubmissions() {
        return getPluginInstance().getSubmissions();
    }

    @Nonnull
    protected SpongeSubmitter getSubmitter(@Nonnull Player player) {
        return getSubmissions().getSubmitter(player);
    }

    @Nullable
    protected SpongeSubmitter getSubmitter(@Nonnull String playerName) {
        return Sponge.getServiceManager().getRegistration(UserStorageService.class).map(providerRegistration -> {
            UserStorageService userStorage = providerRegistration.getProvider();
            return userStorage.get(playerName).map(user -> getSubmitter(user.getUniqueId())).orElse(null);
        }).orElseGet(() -> {
            for (SpongeSubmitter s : getSubmissions().getSubmitters()) {
                if (s.getName().equalsIgnoreCase(playerName)) {
                    return s;
                }
            }

            return null;
        });
    }

    @Nullable
    protected SpongeSubmitter getSubmitter(@Nonnull UUID uuid) {
        return getSubmissions().getSubmitter(uuid);
    }

    @Nonnull
    protected CommandResult playerOnly(CommandSource source) {
        source.sendMessage(Text.builder(Messages.PLAYER_ONLY).color(TextColors.RED).build());
        return CommandResult.empty();
    }
}
