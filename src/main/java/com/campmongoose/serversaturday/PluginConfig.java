package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.Reference.Config;
import java.util.List;
import javax.annotation.Nonnull;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

public final class PluginConfig {

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
