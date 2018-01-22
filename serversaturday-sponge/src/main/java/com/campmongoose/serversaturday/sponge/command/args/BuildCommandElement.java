package com.campmongoose.serversaturday.sponge.command.args;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
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

    public BuildCommandElement(Text key) {
        super(key);
    }

    @Nonnull
    @Override
    public List<String> complete(@Nonnull CommandSource src, @Nonnull CommandArgs args, @Nonnull CommandContext context) {
        if (src instanceof Player) {
            return getSubmitter((Player) src).map(SpongeSubmitter::getBuilds).map(List::stream).map(stream -> stream.map(SpongeBuild::getName).filter(name -> name.startsWith(args.nextIfPresent().orElse(""))).collect(Collectors.toList())).orElse(Collections.emptyList());
        }

        return Collections.emptyList();
    }

    @Nullable
    @Override
    protected Object parseValue(@Nonnull CommandSource source, @Nonnull CommandArgs args) throws ArgumentParseException {
        if (source instanceof Player) {
            Optional<SpongeSubmitter> submitter = getSubmitter((Player) source);
            if (!submitter.isPresent()) {
                throw args.createError(Text.of(TextColors.RED, Messages.PLAYER_NOT_FOUND));
            }

            SpongeBuild build = submitter.get().getBuild(StringUtils.join(args.getAll(), " "));
            if (build == null) {
                throw args.createError(Text.builder(Messages.BUILD_NOT_FOUND).color(TextColors.RED).build());
            }

            return build;
        }

        throw args.createError(Text.builder(Messages.PLAYER_ONLY).color(TextColors.RED).build());
    }
}
