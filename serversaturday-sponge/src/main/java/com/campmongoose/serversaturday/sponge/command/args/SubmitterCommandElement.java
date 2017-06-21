package com.campmongoose.serversaturday.sponge.command.args;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.List;
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

    public static final Text KEY = Text.of("submitter");

    public SubmitterCommandElement() {
        super(KEY);
    }

    @Nullable
    @Override
    protected Object parseValue(@Nonnull CommandSource source, @Nonnull CommandArgs args) throws ArgumentParseException {
        if (source instanceof Player) {
            String playerName = args.next();
            SpongeSubmitter submitter;
            try {
                submitter = getSubmitter(playerName);
            }
            catch (SubmissionsNotLoadedException e) {
                throw args.createError(Text.builder(Messages.PREFIX).append(Text.of(e.getMessage())).color(TextColors.RED).build());
            }

            if (submitter == null) {
                throw args.createError(Text.builder(Messages.PLAYER_NOT_FOUND).color(TextColors.RED).build());
            }

            return submitter;
        }

        throw args.createError(Text.builder(Messages.PLAYER_ONLY).color(TextColors.RED).build());
    }

    @Nonnull
    @Override
    public List<String> complete(@Nonnull CommandSource src, @Nonnull CommandArgs args, @Nonnull CommandContext context) {
        return null;
    }
}
