package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import io.musician101.musicianlibrary.java.minecraft.sponge.gui.chest.SpongeChestGUI;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public abstract class SpongeServerSaturdayChestGUI extends SpongeChestGUI {

    protected SpongeServerSaturdayChestGUI(@Nonnull ServerPlayer player, @Nonnull Component name, int size) {
        super(player, name, size, SpongeServerSaturday.instance().getPluginContainer(), false, true);
    }
}
