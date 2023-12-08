package com.campmongoose.serversaturday.sponge.command.argument;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissionsUtil;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.command.parameter.managed.ValueParameter.Simple;

import static com.campmongoose.serversaturday.sponge.SpongeServerSaturday.getPlugin;

public class SubmitterValueParser implements Simple<Submitter> {

    public static final Value<Submitter> VALUE = Parameter.builder(Submitter.class).key(Commands.PLAYER).addParser(new SubmitterValueParser()).build();

    private SpongeSubmissions getSubmissions() {
        return getPlugin().getSubmissions();
    }

    @Override
    public Optional<? extends Submitter> parseValue(CommandCause commandCause, Mutable reader) throws ArgumentParseException {
        String s = reader.parseString();
        return Optional.ofNullable(SpongeSubmissionsUtil.getSubmitter(s).orElseThrow(() -> new ArgumentParseException(Component.text(Messages.PLAYER_NOT_FOUND), s, reader.cursor())));
    }

    @Override
    public List<CommandCompletion> complete(CommandCause context, String currentInput) {
        return getSubmissions().getSubmitters().stream().map(SpongeSubmissionsUtil::getSubmitterName).filter(s -> s.startsWith(currentInput)).map(CommandCompletion::of).toList();
    }
}
