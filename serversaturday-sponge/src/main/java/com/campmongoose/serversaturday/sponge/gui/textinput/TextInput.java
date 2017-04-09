package com.campmongoose.serversaturday.sponge.gui.textinput;

import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.gui.chest.AbstractSpongeChestGUI;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;

public abstract class TextInput {

    @Nonnull
    protected final Player player;
    @Nullable
    protected final AbstractSpongeChestGUI prevMenu;
    @Nonnull
    protected BiFunction<String, Player, String> biFunction;

    public TextInput(@Nonnull Player player, @Nullable AbstractSpongeChestGUI prevMenu, @Nonnull BiFunction<String, Player, String> biFunction) {
        this.player = player;
        this.prevMenu = prevMenu;
        this.biFunction = biFunction;
        Sponge.getEventManager().registerListeners(SpongeServerSaturday.instance(), this);
    }

    private void end() {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    public void onChat(MessageChannelEvent.Chat event, @First Player player) {
        if (player.getUniqueId().equals(this.player.getUniqueId())) {
            event.setCancelled(true);
            if (biFunction.apply(event.getRawMessage().toPlain(), player) != null) {
                end();
            }
        }
    }
}
