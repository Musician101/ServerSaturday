package com.campmongoose.serversaturday.util;

import com.campmongoose.serversaturday.Reference;
import org.bukkit.ChatColor;

public class UUIDCacheException extends Exception {

    public UUIDCacheException() {
        super(ChatColor.RED + Reference.PREFIX + "Local UUID Cache has not finished initialization.");
    }
}
