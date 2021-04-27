package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import io.musician101.musicianlibrary.java.minecraft.spigot.gui.chest.SpigotChestGUI;
import javax.annotation.Nonnull;
import org.bukkit.entity.Player;

public abstract class SpigotServerSaturdayChestGUI extends SpigotChestGUI<SpigotServerSaturday> {

    protected SpigotServerSaturdayChestGUI(@Nonnull Player player, @Nonnull String name, int size) {
        super(player, name, size, SpigotServerSaturday.instance(), false);
    }
}
