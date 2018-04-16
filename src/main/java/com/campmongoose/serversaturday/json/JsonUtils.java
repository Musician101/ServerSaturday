package com.campmongoose.serversaturday.json;

import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Location;

public class JsonUtils {

    public static Gson GSON = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Build.class, new BuildTypeAdapter()).registerTypeAdapter(Location.class, new LocationTypeAdapter()).registerTypeAdapter(Submitter.class, new SubmitterTypeAdapter()).create();
}
