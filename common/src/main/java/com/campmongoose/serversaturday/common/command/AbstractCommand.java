package com.campmongoose.serversaturday.common.command;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractCommand<M, U extends AbstractCommandUsage, P extends AbstractCommandPermissions, C extends AbstractCommand, S>
{
    protected final List<C> subCommands;
    protected final M description;
    protected final P permissions;
    private final String name;
    protected final U usage;

    protected AbstractCommand(String name, M description, U usage, P permissions, List<C> subCommands)
    {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.permissions = permissions;
        this.subCommands = subCommands;
    }

    public List<C> getSubCommands()
    {
        return subCommands;
    }

    public M getDescription()
    {
        return description;
    }

    public P getPermissions()
    {
        return permissions;
    }

    public String getName()
    {
        return name;
    }

    public U getUsage()
    {
        return usage;
    }

    protected abstract boolean minArgsMet(S source, int argsLength);
}
