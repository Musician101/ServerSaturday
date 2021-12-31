package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.Reference.Config;
import java.util.List;
import javax.annotation.Nonnull;

public final class PluginConfig {

    @Nonnull
    public List<String> getRewards() {
        return getPlugin().getConfig().getStringList(Config.REWARDS);
    }

    private ServerSaturday getPlugin() {
        return ServerSaturday.getInstance();
    }

    public void reload() {
        ServerSaturday plugin = getPlugin();
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
    }
}
