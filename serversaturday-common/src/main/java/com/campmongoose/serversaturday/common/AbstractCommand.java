package com.campmongoose.serversaturday.common;

import com.campmongoose.serversaturday.common.submission.AbstractBuild;
import com.campmongoose.serversaturday.common.submission.AbstractSubmissions;
import com.campmongoose.serversaturday.common.submission.AbstractSubmitter;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractCommand<B extends AbstractBuild<T, L, Q>, I, L, P, Q extends AbstractSubmitter<B, T, L>, S extends AbstractSubmissions<P, Q>, T>
{
    @Nonnull
    protected abstract I getPluginInstance();

    @Nonnull
    protected abstract S getSubmissions();

    @Nonnull
    protected abstract Q getSubmitter(P player);

    @Nullable
    protected abstract Q getSubmitter(UUID uuid);

    @Nullable
    protected abstract Q getSubmitter(String playerName);
}
