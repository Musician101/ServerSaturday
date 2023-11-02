package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.Reference.Config;
import java.util.List;
import org.jetbrains.annotations.NotNull;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

public final class PluginConfig {

    @NotNull
    public List<String> getRewards() {
        return getPlugin().getConfig().getStringList(Config.REWARDS);
    }

    public void reload() {
        ServerSaturday plugin = getPlugin();
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
    }
}
