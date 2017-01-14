package com.campmongoose.serversaturday.common.submission;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractBuild<I, L, S extends AbstractSubmitter>
{
    protected boolean featured = false;
    protected boolean submitted = false;
    protected List<String> description = Collections.singletonList("A Server Saturday Build");
    protected L location;
    protected String name;
    protected String resourcePack = "Vanilla";

    public AbstractBuild(String name)
    {
        this.name = name;
    }

    public AbstractBuild(String name, L location)
    {
        this.name = name;
        this.location = location;
    }

    public boolean featured()
    {
        return featured;
    }

    public void setFeatured(boolean featured)
    {
        this.featured = featured;
    }

    public boolean submitted()
    {
        return submitted;
    }

    public void setSubmitted(boolean submitted)
    {
        this.submitted = submitted;
    }

    public abstract I getMenuRepresentation(S submitter);

    public List<String> getDescription()
    {
        return description;
    }

    public void setDescription(List<String> description)
    {
        this.description = description;
    }

    public L getLocation()
    {
        return location;
    }

    public void setLocation(L location)
    {
        this.location = location;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public abstract Map<String, Object> serialize();

    public String getResourcePack()
    {
        return resourcePack;
    }

    public void setResourcePack(String resourcePack)
    {
        this.resourcePack = resourcePack;
    }
}
