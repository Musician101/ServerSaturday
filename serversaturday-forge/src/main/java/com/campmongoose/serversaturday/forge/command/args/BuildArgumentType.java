package com.campmongoose.serversaturday.forge.command.args;

import com.campmongoose.serversaturday.forge.submission.ForgeBuild;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import net.minecraft.entity.player.ServerPlayerEntity;

public class BuildArgumentType extends SSArgumentType<String> {

    public BuildArgumentType() {

    }

    @Override
    public Collection<String> getExamples() {
        return Collections.singletonList("Fancy Build Name!");
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        S source = context.getSource();
        if (source instanceof ServerPlayerEntity) {
            getSubmitter((ServerPlayerEntity) source).getBuilds().stream().map(ForgeBuild::getName).filter(s -> s.toLowerCase().startsWith(builder.getRemaining().toLowerCase())).forEach(builder::suggest);
        }

        return builder.buildFuture();
    }

    @Override
    public String parse(StringReader reader) {
        return reader.getString();
    }
}
