package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.submission.Build;
import org.bukkit.ChatColor;

public class Reference
{
    public static final String NAME = "ServerSaturday";
    public static final String PREFIX = "[SS] ";

    public static class Commands
    {
        public static final String SS_CMD = "/ss";
    }

    public static class Messages
    {
        public static final String NOT_ENOUGH_ARGS = ChatColor.RED + PREFIX + "Not enough arguments.";
        public static final String NO_PERMISSION = ChatColor.RED + PREFIX + "You don't have permission to run this command.";
        public static final String PLAYER_ONLY = ChatColor.RED + PREFIX + "This is a player only command.";

        public static String locationChanged(Build build)
        {
            return ChatColor.GOLD + PREFIX + "Warp location for " + build.getName() + " updated.";
        }
    }
}
