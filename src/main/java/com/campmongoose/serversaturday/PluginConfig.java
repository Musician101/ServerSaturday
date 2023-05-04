package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.Reference.Config;
import java.util.List;
import javax.annotation.Nonnull;

public final class PluginConfig {

    private ServerSaturday getPlugin() {
        return ServerSaturday.getInstance();
    }

    @Nonnull
    public List<String> getRewards() {
        return getPlugin().getConfig().getStringList(Config.REWARDS);
    }

    public void reload() {
        ServerSaturday plugin = getPlugin();
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
    }
}
