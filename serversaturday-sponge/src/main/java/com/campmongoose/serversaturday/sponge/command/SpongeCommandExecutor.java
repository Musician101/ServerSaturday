package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.AbstractCommand;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.uuid.UUIDUtils;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.io.IOException;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public abstract class SpongeCommandExecutor extends AbstractCommand<SpongeBuild, SpongeServerSaturday, Location<World>, Player, SpongeSubmitter, SpongeSubmissions, ItemStack> implements CommandExecutor {

    @Nonnull
    @Override
    protected SpongeServerSaturday getPluginInstance() {
        return SpongeServerSaturday.instance();
    }

    @Nonnull
    @Override
    protected SpongeSubmissions getSubmissions() {
        return getPluginInstance().getSubmissions();
    }

    @Nonnull
    @Override
    protected SpongeSubmitter getSubmitter(@Nonnull Player player) {
        return getSubmissions().getSubmitter(player);
    }

    @Nullable
    @Override
    protected SpongeSubmitter getSubmitter(@Nonnull String playerName) {
        try {
            return getSubmitter(UUIDUtils.getUUIDOf(playerName));
        }
        catch (IOException e) {
            for (SpongeSubmitter s : getSubmissions().getSubmitters()) {
                if (s.getName().equalsIgnoreCase(playerName)) {
                    return s;
                }
            }
        }

        return null;
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
