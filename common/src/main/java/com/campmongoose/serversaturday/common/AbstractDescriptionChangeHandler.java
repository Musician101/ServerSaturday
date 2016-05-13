package com.campmongoose.serversaturday.common;

import com.campmongoose.serversaturday.common.submission.AbstractBuild;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public abstract class AbstractDescriptionChangeHandler<B extends AbstractBuild, C, D, E, I, M, T, V>
{
    private Map<UUID, B> builds = new HashMap<>();
    private Map<UUID, I> itemStacks = new HashMap<>();
    private Map<UUID, T> taskIds = new HashMap<>();

    protected AbstractDescriptionChangeHandler()
    {

    }

    protected abstract I getBook(UUID uuid, B build);

    protected abstract void add(UUID uuid, B build);

    protected abstract void remove(UUID uuid);

    protected abstract boolean isSameBook(M bookData, UUID uuid, B build);

    public boolean containsPlayer(UUID uuid)
    {
        return builds.containsKey(uuid) || itemStacks.containsKey(uuid) || taskIds.containsKey(uuid);
    }

    public abstract void bookInteract(E event);

    public abstract void clickBook(C event);

    public abstract void dropBook(D event);

    public abstract void editBook(V event);
}
