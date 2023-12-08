package com.campmongoose.serversaturday.sponge.command.argument;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import io.leangen.geantyref.TypeToken;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.command.parameter.managed.ValueParameter.Simple;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import static com.campmongoose.serversaturday.sponge.SpongeServerSaturday.getPlugin;

public class BuildValueParser implements Simple<Map<UUID, Build>> {

    public static final Value<Map<UUID, Build>> VALUE = Parameter.builder(new TypeToken<Map<UUID, Build>>(){}).key(Commands.BUILD).addParser(new BuildValueParser()).build();

    public SpongeSubmissions getSubmissions() {
        return getPlugin().getSubmissions();
    }

    @Override
    public Optional<? extends Map<UUID, Build>> parseValue(CommandCause commandCause, Mutable reader) {
        Map<UUID, Build> builds = new HashMap<>();
        getSubmissions().getSubmitters().forEach(submitter -> submitter.getBuild(reader.remaining()).ifPresent(build -> builds.put(submitter.getUUID(), build)));
        return Optional.of(builds);
    }

    @Override
    public List<CommandCompletion> complete(CommandCause cause, String currentInput) {
        return getSubmissions().getSubmitter(((ServerPlayer) cause.subject()).uniqueId()).getBuilds().stream().map(Build::getName).filter(build -> build.startsWith(currentInput)).map(CommandCompletion::of).toList();
    }
}
