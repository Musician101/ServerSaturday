package com.campmongoose.serversaturday.common.submission;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractSubmitter<B extends AbstractBuild, I, L>
{
    protected final Map<String, B> builds = new HashMap<>();
    protected String name;
    protected final UUID uuid;

    protected AbstractSubmitter(UUID uuid)
    {
        this.uuid = uuid;
    }

    protected AbstractSubmitter(String name, UUID uuid)
    {
        this.name = name;
        this.uuid = uuid;
    }

    public B getBuild(String name)
    {
        return builds.get(name);
    }

    public abstract B newBuild(String name, L location);

    public abstract I getMenuRepresentation();

    public List<B> getBuilds()
    {
        return new ArrayList<>(builds.values());
    }

    public abstract void openMenu(int page, UUID uuid);

    public void removeBuild(String name)
    {
        builds.remove(name);
    }

    public abstract void save(File file);

    public abstract void updateBuildDescription(B build, List<String> description);

    public abstract void updateBuildName(B build, String newName);

    public abstract void updateBuildResourcePack(B build, String resourcePack);

    public String getName()
    {
        return name;
    }

    public UUID getUUID()
    {
        return uuid;
    }
}
