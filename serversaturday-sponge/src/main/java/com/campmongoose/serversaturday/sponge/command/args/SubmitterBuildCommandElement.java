package com.campmongoose.serversaturday.sponge.command.args;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.AbstractMap.SimpleEntry;
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

public class SubmitterBuildCommandElement extends SSCommandElement {

    public static final Text KEY = Text.of("submitter_build");

    public SubmitterBuildCommandElement() {
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

            if (!args.hasNext()) {
                return new SimpleEntry<>(submitter, (SpongeBuild) null);
            }

            SpongeBuild build = submitter.getBuild(getBuild(args));
            if (build == null) {
                throw args.createError(Text.builder(Messages.BUILD_NOT_FOUND).color(TextColors.RED).build());
            }

            return new SimpleEntry<>(submitter, build);
        }

        throw args.createError(Text.builder(Messages.PLAYER_ONLY).color(TextColors.RED).build());
    }

    @Nonnull
    @Override
    public List<String> complete(@Nonnull CommandSource src, @Nonnull CommandArgs args, @Nonnull CommandContext context) {
        if (src instanceof Player) {
            List<String> argsList = args.getAll();
            try {
                List<String> names = getSubmissions().getSubmitters().stream().map(SpongeSubmitter::getName).collect(Collectors.toList());
                if (argsList.isEmpty()) {
                    return names;
                }
                else if (argsList.size() == 1) {
                    return names.stream().filter(name -> name.toLowerCase().startsWith(argsList.get(0).toLowerCase())).collect(Collectors.toList());
                }
                else if (argsList.size() == 2) {
                    SpongeSubmitter submitter = getSubmitter(argsList.get(0));
                    if (submitter != null) {
                        return submitter.getBuilds().stream().map(SpongeBuild::getName).filter(name ->
                                name.toLowerCase().startsWith(StringUtils.join(argsList.subList(1, argsList.size() - 1), " ").toLowerCase())).collect(Collectors.toList());
                    }
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
        return Text.of("[" + Commands.PLAYER + "] [" + Commands.BUILD + "...]");
    }
}
