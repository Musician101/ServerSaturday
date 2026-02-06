package com.campmongoose.serversaturday.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class OfflinePlayerArgumentType implements CustomArgumentType<OfflinePlayer, String> {

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public OfflinePlayer parse(StringReader stringReader) throws CommandSyntaxException {
        return Bukkit.getOfflinePlayer(stringReader.readString());
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}
