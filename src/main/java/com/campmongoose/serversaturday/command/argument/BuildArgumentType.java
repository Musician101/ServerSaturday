package com.campmongoose.serversaturday.command.argument;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submissions;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;

public class BuildArgumentType implements ArgumentType<Map<UUID, Build>> {

    public Submissions getSubmissions() {
        return ServerSaturday.getInstance().getSubmissions();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        getSubmissions().getSubmitter((Player) context.getSource()).getBuilds().stream().map(Build::getName).filter(build -> build.startsWith(builder.getRemaining())).forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public Map<UUID, Build> parse(StringReader stringReader) {
        Map<UUID, Build> builds = new HashMap<>();
        getSubmissions().getSubmitters().forEach(submitter -> submitter.getBuild(stringReader.getRemaining()).ifPresent(build -> builds.put(submitter.getUUID(), build)));
        stringReader.setCursor(stringReader.getTotalLength());
        return builds;
    }
}
