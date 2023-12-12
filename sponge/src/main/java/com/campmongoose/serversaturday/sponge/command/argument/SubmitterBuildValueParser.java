package com.campmongoose.serversaturday.sponge.command.argument;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import java.util.List;
import java.util.Optional;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.command.parameter.managed.ValueParameter.Simple;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import static com.campmongoose.serversaturday.sponge.SpongeServerSaturday.getPlugin;

public class SubmitterBuildValueParser implements Simple<Build> {

    public static final Value<Build> VALUE = Parameter.builder(Parameter.key(Commands.BUILD, Build.class)).addParser(new SubmitterBuildValueParser()).build();

    public SpongeSubmissions getSubmissions() {
        return getPlugin().getSubmissions();
    }

    @Override
    public Optional<? extends Build> parseValue(CommandCause commandCause, Mutable reader) {
        if (commandCause instanceof ServerPlayer player) {
            return getSubmissions().getSubmitter(player.uniqueId()).getBuild(reader.remaining());
        }

        return Optional.empty();
    }

    @Override
    public List<CommandCompletion> complete(CommandCause cause, String currentInput) {
        if (cause instanceof ServerPlayer player) {
            return getSubmissions().getSubmitter(player.uniqueId()).getBuilds().stream().map(Build::getName).filter(b -> currentInput.isBlank() || b.startsWith(currentInput)).map(CommandCompletion::of).toList();
        }

        return List.of();
    }
}
