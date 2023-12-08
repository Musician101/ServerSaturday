package com.campmongoose.serversaturday.paper.command.argument;


import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.paper.submission.PaperSubmissionsUtil;
import com.campmongoose.serversaturday.paper.submission.PaperSubmissions;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.CompletableFuture;

import static com.campmongoose.serversaturday.paper.PaperServerSaturday.getPlugin;

public class SubmitterArgumentType implements ArgumentType<Submitter> {

    private PaperSubmissions getSubmissions() {
        return getPlugin().getSubmissions();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        getSubmissions().getSubmitters().stream().map(PaperSubmissionsUtil::getSubmitterName).filter(s -> s.startsWith(builder.getRemaining())).forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public Submitter parse(StringReader stringReader) throws CommandSyntaxException {
        return PaperSubmissionsUtil.getSubmitter(stringReader.readString()).orElseThrow(() -> new SimpleCommandExceptionType(() -> Reference.Messages.PLAYER_NOT_FOUND).create());
    }
}
