package com.campmongoose.serversaturday.sponge.command.args;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Submissions;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SubmitterCommandElement extends SSCommandElement {

    public static final Text KEY = Text.of(Commands.PLAYER);

    public SubmitterCommandElement() {
        super(KEY);
    }

    @Nonnull
    @Override
    public List<String> complete(@Nonnull CommandSource src, @Nonnull CommandArgs args, @Nonnull CommandContext context) {
        return getSubmissions().map(Submissions::getSubmitters).map(List::stream).map(stream -> stream.map(Submitter::getName).filter(name -> name.startsWith(args.nextIfPresent().orElse(""))).collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    @Nullable
    @Override
    protected Object parseValue(@Nonnull CommandSource source, @Nonnull CommandArgs args) throws ArgumentParseException {
        if (source instanceof Player) {
            String playerName = args.next();
            Optional<SpongeSubmitter> submitter = getSubmitter(playerName);
            if (submitter.isPresent()) {
                return submitter.get();
            }

            throw args.createError(Text.builder(Messages.PLAYER_NOT_FOUND).color(TextColors.RED).build());
        }

        throw args.createError(Text.builder(Messages.PLAYER_ONLY).color(TextColors.RED).build());
    }
}
