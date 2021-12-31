package com.campmongoose.serversaturday.spigot;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public interface PermissionCheckExecutor extends TabExecutor {

    boolean hasPermission(CommandSender sender);

    @Override
    default List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings) {
        return Collections.emptyList();
    }
}
