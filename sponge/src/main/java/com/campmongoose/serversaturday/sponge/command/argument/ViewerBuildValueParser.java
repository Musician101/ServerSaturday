package com.campmongoose.serversaturday.sponge.command.argument;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissionsUtil;
import java.util.List;
import java.util.Optional;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.command.parameter.managed.ValueParameter.Simple;

import static com.campmongoose.serversaturday.sponge.SpongeServerSaturday.getPlugin;

public class ViewerBuildValueParser implements Simple<Build> {

    public static final Value<Build> VALUE = Parameter.builder(Build.class).key(Commands.BUILD).addParser(new ViewerBuildValueParser()).optional().build();

    public SpongeSubmissions getSubmissions() {
        return getPlugin().getSubmissions();
    }

    @Override
    public Optional<? extends Build> parseValue(CommandCause commandCause, Mutable reader) {
        String submitter = reader.input().split(" ")[2];
        return SpongeSubmissionsUtil.getSubmitter(submitter).flatMap(s -> s.getBuild(reader.remaining()));
    }

    @Override
    public List<CommandCompletion> complete(CommandCause cause, String currentInput) {
        return getSubmissions().getSubmitters().stream().map(Submitter::getBuilds).flatMap(List::stream).map(Build::getName).filter(build -> build.startsWith(currentInput)).map(CommandCompletion::of).toList();
    }
}
