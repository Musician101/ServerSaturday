package com.campmongoose.serversaturday.common.command;

import java.util.function.Function;
import javax.annotation.Nonnull;

public abstract class AbstractCommandPermissions<S> {

    private boolean isPlayerOnly;
    @Nonnull
    private String permissionNode;
    private Function<S, Boolean> permissionTester;

    public AbstractCommandPermissions(@Nonnull String permissionNode, boolean isPlayerOnly, Function<S, Boolean> permissionTester) {
        this.permissionNode = permissionNode;
        this.isPlayerOnly = isPlayerOnly;
        this.permissionTester = permissionTester;
    }

    @Nonnull
    public String getPermissionNode() {
        return permissionNode;
    }

    public boolean isPlayerOnly() {
        return isPlayerOnly;
    }

    public boolean testPermission(S source) {
        return permissionTester.apply(source);
    }
}
