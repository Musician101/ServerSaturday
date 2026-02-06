package com.campmongoose.serversaturday.command.argument;

import com.campmongoose.serversaturday.submission.Submissions;
import com.campmongoose.serversaturday.submission.Submitter;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

@NullMarked
public class SubmitterArgumentType implements CustomArgumentType.Converted<Submitter, String> {

    private Submissions getSubmissions() {
        return getPlugin().getSubmissions();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        getSubmissions().getSubmitters().stream().map(Submitter::name).filter(s -> s.startsWith(builder.getRemaining())).forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public Submitter convert(String nativeType) throws CommandSyntaxException {
        return getSubmissions().getSubmitter(nativeType).orElseThrow(() -> new SimpleCommandExceptionType(() -> "Could not find a player with that name.").create());
    }
}
