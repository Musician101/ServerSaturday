package com.campmongoose.serversaturday.submission;

import com.campmongoose.serversaturday.Reference;
import org.bukkit.ChatColor;

public class SubmissionsNotLoadedException extends Exception {

    public SubmissionsNotLoadedException() {
        super(ChatColor.RED + Reference.PREFIX + "Submissions have not loaded.");
    }
}
