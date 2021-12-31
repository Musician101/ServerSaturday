package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submissions;
import com.campmongoose.serversaturday.submission.Submitter;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;

public class BuildFromSubmitterArgumentType implements ArgumentType<Build> {

    public Submissions getSubmissions() {
        return ServerSaturday.getInstance().getSubmissions();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        StringReader reader = new StringReader(builder.getInput());
        try {
            for (int i = 0; i < 2; i++) {
                reader.readStringUntil(' ');
            }

            getSubmissions().getSubmitter(reader.readString()).ifPresent(submitter -> submitter.getBuilds().stream().map(Build::getName).filter(build -> build.startsWith(builder.getRemaining())).forEach(builder::suggest));
        }
        catch (CommandSyntaxException ignored) {

        }

        return builder.buildFuture();
    }

    @Override
    public Build parse(StringReader stringReader) throws CommandSyntaxException {
        stringReader.setCursor(0);
        for (int i = 0; i < 2; i++) {
            stringReader.readStringUntil(' ');
        }

        Submitter submitter = getSubmissions().getSubmitter(stringReader.readString()).orElseThrow(() -> new SimpleCommandExceptionType(() -> Messages.PLAYER_NOT_FOUND).create());
        stringReader.setCursor(stringReader.getCursor() + 1);
        return submitter.getBuild(stringReader.readUnquotedString()).orElseThrow(() -> new SimpleCommandExceptionType(() -> Messages.BUILD_DOES_NOT_EXIST).create());
    }
}
