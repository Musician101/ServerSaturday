package com.campmongoose.serversaturday.command.argument;

import com.campmongoose.serversaturday.command.argument.BuildArgumentType.Holder;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

@NullMarked
public abstract class BuildArgumentType implements CustomArgumentType.Converted<Holder, String> {

    public static final BuildArgumentType SUBMITTER = new BuildArgumentType() {

        @Override
        protected <S> Submitter getSubmitter(CommandContext<S> context) throws CommandSyntaxException {
            if (context.getSource() instanceof CommandSourceStack css) {
                return getPlugin().getSubmissions().getSubmitter((Player) css.getSender());
            }

            throw new SimpleCommandExceptionType(() -> "Parameterized type of CommandContext is not io.papermc.paper.command.brigadier.CommandSourceStack").create();
        }
    };
    public static final BuildArgumentType VIEWER = new BuildArgumentType() {

        @Override
        protected <S> Submitter getSubmitter(CommandContext<S> context) {
            return context.getArgument("player", Submitter.class);
        }
    };

    protected abstract <S> Submitter getSubmitter(CommandContext<S> context) throws CommandSyntaxException;

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        try {
            getSubmitter(context).builds().stream().map(Build::name).filter(build -> build.startsWith(builder.getRemaining())).forEach(builder::suggest);
        }
        catch (CommandSyntaxException ignored) {

        }

        return builder.buildFuture();
    }

    @Override
    public Holder convert(String nativeType) {
        return submitter -> submitter.getBuild(nativeType);
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    public interface Holder {

        Optional<Build> get(Submitter submitter);
    }
}
