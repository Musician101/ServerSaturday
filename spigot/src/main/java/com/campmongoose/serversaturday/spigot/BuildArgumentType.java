package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.musician101.musicianlibrary.java.storage.DataStorage;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;

public class BuildArgumentType implements ArgumentType<Map<UUID, Build>> {

    public DataStorage<?, Submitter> getSubmissions() {
        return SpigotServerSaturday.instance().getSubmissions();
    }

    @Override
    public Map<UUID, Build> parse(StringReader stringReader) throws CommandSyntaxException {
        Map<UUID, Build> builds = new HashMap<>();
        getSubmissions().getData().forEach(submitter -> {
            Build build = submitter.getBuild(stringReader.getRemaining());
            if (build != null) {
                builds.put(submitter.getUUID(), build);
            }
        });

        return builds;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Player player = (Player) context.getSource();
        Optional<Submitter> optional = getSubmissions().getEntry(s -> s.getUUID().equals(player.getUniqueId()));
        if (optional.isPresent()) {
            Submitter submitter = optional.get();
            submitter.getBuilds().stream().map(Build::getName).filter(build -> build.startsWith(builder.getRemaining())).forEach(builder::suggest);
        }

        return builder.buildFuture();
    }
}
