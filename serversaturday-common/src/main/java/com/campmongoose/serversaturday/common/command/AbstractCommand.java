package com.campmongoose.serversaturday.common.command;

import com.campmongoose.serversaturday.common.submission.AbstractBuild;
import com.campmongoose.serversaturday.common.submission.AbstractSubmissions;
import com.campmongoose.serversaturday.common.submission.AbstractSubmitter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractCommand<A extends AbstractCommandArgument<M>, B extends AbstractBuild<T, L, Q>,
        C extends AbstractCommand<A, B, C, I, L, M, P, Q, R, S, T, U, Y, Z>, I, L, M, P,
        Q extends AbstractSubmitter<B, T, L>, R, S extends AbstractSubmissions<P, Q>, T,
        U extends AbstractCommandUsage<A, M, Z>, Y extends AbstractCommandPermissions<Z>, Z> {

    protected BiFunction<Z, List<String>, R> executor;
    @Nonnull
    protected M description;
    @Nonnull
    protected String name;
    protected Y permissions;
    protected U usage;

    public AbstractCommand(@Nonnull String name, @Nonnull M description) {
        this.name = name;
        this.description = description;
    }

    public abstract C getHelpCommand();

    @Nonnull
    public M getDescription() {
        return description;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public U getUsage() {
        return usage;
    }

    @Nonnull
    public Y getPermissions() {
        return permissions;
    }

    @Nonnull
    protected abstract M getHelp();

    @Nonnull
    protected abstract I getPluginInstance();

    @Nonnull
    protected abstract S getSubmissions();

    @Nonnull
    protected abstract Q getSubmitter(@Nonnull P player);

    @Nullable
    protected abstract Q getSubmitter(@Nonnull UUID uuid);

    @Nullable
    protected abstract Q getSubmitter(@Nonnull String playerName);

    protected List<String> moveArguments(List<String> args) {
        if (!args.isEmpty()) {
            args.remove(0);
        }

        return args;
    }

    protected String[] moveArguments(String[] args) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, args);
        if (!list.isEmpty()) {
            list.remove(0);
        }

        return list.toArray(new String[list.size()]);
    }
}
