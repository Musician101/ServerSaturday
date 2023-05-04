package com.campmongoose.serversaturday.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class OfflinePlayerArgumentType implements ArgumentType<OfflinePlayer> {

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public OfflinePlayer parse(StringReader stringReader) throws CommandSyntaxException {
        return Bukkit.getOfflinePlayer(stringReader.readString());
    }
}
