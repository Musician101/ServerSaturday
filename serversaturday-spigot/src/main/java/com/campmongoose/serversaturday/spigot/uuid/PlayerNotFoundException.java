package com.campmongoose.serversaturday.spigot.uuid;

import java.util.UUID;

public class PlayerNotFoundException extends Exception {

    public PlayerNotFoundException(String name) {
        super("A player with name " + name + " does not exist.");
    }

    public PlayerNotFoundException(UUID uuid) {
        super("A player with uuid " + uuid.toString() + " does not exist.");
    }
}
