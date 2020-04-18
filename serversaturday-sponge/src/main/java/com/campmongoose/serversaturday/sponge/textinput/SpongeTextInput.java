package com.campmongoose.serversaturday.sponge.textinput;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class SpongeTextInput {

    private static final Map<UUID, BiConsumer<Player, String>> PLAYERS = new HashMap<>();

    public static void addPlayer(Player player, BiConsumer<Player, String> action) {
        PLAYERS.put(player.getUniqueId(), action);
    }

    @Listener
    public void onPlayerChat(MessageEvent event, @First Player player) {
        if (!PLAYERS.containsKey(player.getUniqueId())) {
            return;
        }

        event.setMessageCancelled(true);
        PLAYERS.remove(player.getUniqueId()).accept(player, event.getMessage().toPlain());
    }

    @Listener
    public void onPlayerDisconnect(ClientConnectionEvent.Disconnect event, @First Player player) {
        PLAYERS.remove(player.getUniqueId());
    }
}
