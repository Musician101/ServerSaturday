package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.ServerSaturday;
import io.musician101.musigui.spigot.chest.SpigotChestGUI;
import javax.annotation.Nonnull;
import org.bukkit.entity.Player;

class ServerSaturdayChestGUI extends SpigotChestGUI<ServerSaturday> {

    protected ServerSaturdayChestGUI(@Nonnull Player player, @Nonnull String name, int size) {
        super(player, name, size, ServerSaturday.getInstance(), false);
    }
}
