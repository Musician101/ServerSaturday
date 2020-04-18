package com.campmongoose.serversaturday.sponge.command.args;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.Collections;
import java.util.List;
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

public class BuildCommandElement extends SSCommandElement {

    public static final Text KEY = Text.of(Commands.BUILD);

    public BuildCommandElement() {
        super(KEY);
    }

    @Nonnull
    @Override
    public List<String> complete(@Nonnull CommandSource src, @Nonnull CommandArgs args, @Nonnull CommandContext context) {
        if (src instanceof Player) {
            return getSubmitter((Player) src).getBuilds().stream().map(SpongeBuild::getName).filter(name -> name.startsWith(args.nextIfPresent().orElse(""))).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Nullable
    @Override
    protected Object parseValue(@Nonnull CommandSource source, @Nonnull CommandArgs args) throws ArgumentParseException {
        if (source instanceof Player) {
            SpongeSubmitter submitter = getSubmitter((Player) source);
            SpongeBuild build = submitter.getBuild(getBuild(args));
            if (build == null) {
                throw args.createError(Text.of(TextColors.RED, Messages.BUILD_NOT_FOUND));
            }

            return build;
        }

        throw args.createError(Text.of(TextColors.RED, Messages.PLAYER_ONLY));
    }
}
