package com.campmongoose.serversaturday.common.submission;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractSubmitter<B extends AbstractBuild, I, L>
{
    protected final Map<String, B> builds = new HashMap<>();
    @Nonnull
    protected final String name;
    @Nonnull
    protected final UUID uuid;

    protected AbstractSubmitter(@Nonnull String name, @Nonnull UUID uuid)
    {
        this.name = name;
        this.uuid = uuid;
    }

    @Nullable
    public B getBuild(@Nonnull String name)
    {
        return builds.get(name);
    }

    @Nonnull
    public abstract B newBuild(@Nonnull String name, @Nonnull L location);

    @Nonnull
    public abstract I getMenuRepresentation();

    @Nonnull
    public List<B> getBuilds()
    {
        return new ArrayList<>(builds.values());
    }

    public boolean removeBuild(@Nonnull String name)
    {
        return builds.remove(name) != null;
    }

    public abstract void save(@Nonnull File file);

    public abstract void updateBuildDescription(@Nonnull B build, @Nonnull List<String> description);

    public abstract void updateBuildName(@Nonnull B build, @Nonnull String newName);

    public abstract void updateBuildResourcePack(@Nonnull B build, @Nonnull String resourcePack);

    @Nonnull
    public String getName()
    {
        return name;
    }

    @Nonnull
    public UUID getUUID()
    {
        return uuid;
    }
}
