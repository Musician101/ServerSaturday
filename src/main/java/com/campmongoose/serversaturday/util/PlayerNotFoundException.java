package com.campmongoose.serversaturday.util;

import com.campmongoose.serversaturday.Reference;
import java.util.UUID;
import org.bukkit.ChatColor;

public class PlayerNotFoundException extends Exception {

    public PlayerNotFoundException(UUID uuid) {
        super(ChatColor.RED + Reference.PREFIX + "Player with UUID " + uuid.toString() + " does not exist.");
    }

}
