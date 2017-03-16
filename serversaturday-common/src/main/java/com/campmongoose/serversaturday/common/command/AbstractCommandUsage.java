package com.campmongoose.serversaturday.common.command;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.annotation.Nonnull;

public abstract class AbstractCommandUsage<A extends AbstractCommandArgument<M>, M, S> {

    private int minArgs;
    @Nonnull
    private M usage;
    protected BiFunction<S, Integer, Boolean> minArgsChecker;

    public AbstractCommandUsage(int minArgs, List<A> arguments, Function<List<A>, M> argumentParser) {
        this.minArgs = minArgs;
        this.usage = argumentParser.apply(arguments);
    }

    public boolean minArgsMet(S source, int argsLength) {
        return minArgsChecker.apply(source, argsLength);
    }

    public int getMinArgs() {
        return minArgs;
    }

    @Nonnull
    public M getUsage() {
        return usage;
    }
}
