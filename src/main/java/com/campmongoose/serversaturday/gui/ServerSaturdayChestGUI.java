package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.ServerSaturday;
import io.musician101.musicianlibrary.java.minecraft.spigot.gui.chest.SpigotChestGUI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

class ServerSaturdayChestGUI extends SpigotChestGUI<ServerSaturday> {

    protected ServerSaturdayChestGUI(@NotNull Player player, @NotNull String name, int size) {
        super(player, name, size, ServerSaturday.getInstance(), false);
    }
}
