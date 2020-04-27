package com.campmongoose.serversaturday.forge.command.args;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SubmitterArgumentType extends SSArgumentType<ForgeSubmitter> {

    @Override
    public CompletableFuture<Suggestions> listSuggestions(CommandContext context, SuggestionsBuilder builder) {
        getSubmissions().getSubmitters().stream().map(ForgeSubmitter::getName).filter(name -> name.toLowerCase().startsWith(builder.getRemaining().toLowerCase())).forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public ForgeSubmitter parse(StringReader reader) throws CommandSyntaxException {
        String input = reader.readString();
        Optional<ForgeSubmitter> submitter = getSubmitter(input);
        if (submitter.isPresent()) {
            return submitter.get();
        }

        throw new CommandSyntaxException(new SimpleCommandExceptionType(() -> Messages.PLAYER_NOT_FOUND), () -> Messages.PLAYER_NOT_FOUND);
    }
}
