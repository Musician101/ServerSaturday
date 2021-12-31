package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import io.musician101.musicianlibrary.java.storage.DataStorage;
import java.util.Optional;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public abstract class SSCommandExecutor implements CommandExecutor {

    @Nonnull
    protected SpongeServerSaturday getPlugin() {
        return SpongeServerSaturday.instance();
    }

    @Nonnull
    protected DataStorage<?, Submitter<Component>> getSubmissions() {
        return getPlugin().getSubmissions();
    }

    @Nonnull
    protected Submitter<Component> getSubmitter(@Nonnull ServerPlayer player) {
        DataStorage<?, Submitter<Component>> submissions = getSubmissions();
        Optional<Submitter<Component>> optional = submissions.getEntry(s -> player.uniqueId().equals(s.getUUID()));
        if (optional.isPresent()) {
            return optional.get();
        }

        Submitter<Component> submitter = new Submitter<>(player.name(), player.uniqueId());
        submissions.addEntry(submitter);
        return submitter;
    }

    @Nonnull
    protected CommandResult playerOnly() {
        return CommandResult.error(Component.text(Messages.PLAYER_ONLY).color(NamedTextColor.RED));
    }
}
