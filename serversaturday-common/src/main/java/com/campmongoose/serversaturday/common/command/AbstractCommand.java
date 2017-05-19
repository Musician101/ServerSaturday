package com.campmongoose.serversaturday.common.command;

import com.campmongoose.serversaturday.common.submission.AbstractBuild;
import com.campmongoose.serversaturday.common.submission.AbstractSubmissions;
import com.campmongoose.serversaturday.common.submission.AbstractSubmitter;
import com.campmongoose.serversaturday.common.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.common.uuid.MojangAPIException;
import com.campmongoose.serversaturday.common.uuid.PlayerNotFoundException;
import com.campmongoose.serversaturday.common.uuid.UUIDCache;
import com.campmongoose.serversaturday.common.uuid.UUIDCacheException;
import java.io.IOException;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractCommand<B extends AbstractBuild<T, L, Q>, I, L, P, Q extends AbstractSubmitter<B, T, L>, S extends AbstractSubmissions<P, Q>, T> {

    @Nonnull
    protected abstract I getPluginInstance();

    @Nonnull
    protected abstract UUIDCache getUUIDCache() throws UUIDCacheException;

    @Nonnull
    protected abstract S getSubmissions() throws UUIDCacheException, SubmissionsNotLoadedException;

    @Nonnull
    protected abstract Q getSubmitter(@Nonnull P player) throws SubmissionsNotLoadedException;

    @Nullable
    protected abstract Q getSubmitter(@Nonnull UUID uuid) throws SubmissionsNotLoadedException;

    @Nullable
    protected abstract Q getSubmitter(@Nonnull String playerName) throws UUIDCacheException, MojangAPIException, IOException, PlayerNotFoundException, SubmissionsNotLoadedException;
}
