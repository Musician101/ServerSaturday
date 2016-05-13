package com.campmongoose.serversaturday.common.command;

public class AbstractCommandPermissions<M>
{
    private final boolean isPlayerOnly;
    private final M noPermission;
    private final String permissionNode;
    private final M playerOnly;

    protected AbstractCommandPermissions(String permissionNode, boolean isPlayerOnly, M noPermission, M playerOnly)
    {
        this.permissionNode = permissionNode;
        this.isPlayerOnly = isPlayerOnly;
        this.noPermission = noPermission;
        this.playerOnly = playerOnly;
    }

    public boolean isPlayerOnly()
    {
        return isPlayerOnly;
    }

    public M getNoPermission()
    {
        return noPermission;
    }

    public String getPermissionNode()
    {
        return permissionNode;
    }

    public M getPlayerOnly()
    {
        return playerOnly;
    }
}
