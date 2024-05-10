package com.campmongoose.serversaturday.command.argument;

import com.campmongoose.serversaturday.command.argument.BuildArgumentType.Holder;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

public abstract class BuildArgumentType implements ArgumentType<Holder> {

    public static final BuildArgumentType SUBMITTER = new BuildArgumentType() {

        @NotNull
        @Override
        protected <S> Submitter getSubmitter(@NotNull CommandContext<S> context) {
            return getPlugin().getSubmissions().getSubmitter((Player) context.getSource());
        }
    };
    public static final BuildArgumentType VIEWER = new BuildArgumentType() {

        @NotNull
        @Override
        protected <S> Submitter getSubmitter(@NotNull CommandContext<S> context) {
            return context.getArgument("player", Submitter.class);
        }
    };

    @NotNull
    protected abstract <S> Submitter getSubmitter(@NotNull CommandContext<S> context);

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        getSubmitter(context).getBuilds().stream().map(Build::getName).filter(build -> build.startsWith(builder.getRemaining())).forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public Holder parse(StringReader stringReader) {
        String string = stringReader.getRemaining();
        stringReader.setCursor(stringReader.getTotalLength());
        return submitter -> submitter.getBuild(string);
    }

    public interface Holder {

        @NotNull
        Optional<Build> get(@NotNull Submitter submitter);
    }
}
