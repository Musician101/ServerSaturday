package com.campmongoose.serversaturday.util;

import com.campmongoose.serversaturday.Reference;
import org.bukkit.ChatColor;

public class MojangAPIException extends Exception {

    public MojangAPIException() {
        super(ChatColor.RED + Reference.PREFIX + "An error occurred while pinging the MojangAPI.");
    }
}
