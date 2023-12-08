package com.campmongoose.serversaturday.paper.command.argument;

import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.paper.submission.PaperSubmissions;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.campmongoose.serversaturday.paper.PaperServerSaturday.getPlugin;

public class BuildArgumentType implements ArgumentType<Map<UUID, Build>> {

    public PaperSubmissions getSubmissions() {
        return getPlugin().getSubmissions();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        getSubmissions().getSubmitter(((Player) context.getSource()).getUniqueId()).getBuilds().stream().map(Build::getName).filter(build -> build.startsWith(builder.getRemaining())).forEach(builder::suggest);
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
