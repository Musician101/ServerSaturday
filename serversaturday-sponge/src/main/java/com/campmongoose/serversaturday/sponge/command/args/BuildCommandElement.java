package com.campmongoose.serversaturday.sponge.command.args;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.Collections;
import java.util.List;
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

    public static Text KEY = Text.of("build");

    public BuildCommandElement() {
        super(KEY);
    }

    public BuildCommandElement(Text key) {
        super(key);
    }

    @Nullable
    @Override
    protected Object parseValue(@Nonnull CommandSource source, @Nonnull CommandArgs args) throws ArgumentParseException {
        if (source instanceof Player) {
            SpongeSubmitter submitter;
            try {
                submitter = getSubmitter((Player) source);
            }
            catch (SubmissionsNotLoadedException e) {
                throw args.createError(Text.join(Text.builder(Messages.PREFIX).append(Text.of(e.getMessage())).color(TextColors.RED).build(), Text.builder(e.getMessage()).color(TextColors.RED).build()));
            }

            SpongeBuild build = submitter.getBuild(StringUtils.join(args.getAll(), " "));
            if (build == null) {
                throw args.createError(Text.builder(Messages.BUILD_NOT_FOUND).color(TextColors.RED).build());
            }

            return build;
        }

        throw args.createError(Text.builder(Messages.PLAYER_ONLY).color(TextColors.RED).build());
    }

    @Nonnull
    @Override
    public List<String> complete(@Nonnull CommandSource src, @Nonnull CommandArgs args, @Nonnull CommandContext context) {
        if (src instanceof Player) {
            try {
                if (args.hasNext()) {
                    return getSubmitter((Player) src).getBuilds().stream().map(SpongeBuild::getName).filter(name -> name.startsWith(args.getRaw())).collect(Collectors.toList());
                }
            }
            catch (SubmissionsNotLoadedException e) {
                src.sendMessage(Text.builder(Messages.PREFIX).append(Text.of(e.getMessage())).color(TextColors.RED).build());
            }
        }

        return Collections.emptyList();
    }

    @Nonnull
    @Override
    public Text getUsage(CommandSource src) {
        return Text.of("<" + Commands.BUILD + ">");
    }
}
