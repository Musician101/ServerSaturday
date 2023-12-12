package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.Config;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.sponge.command.SSCommand;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.google.inject.Inject;
import io.musician101.spongecmd.CMDExecutor;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

@Plugin("serversaturday")
public class SpongeServerSaturday extends ServerSaturday<SpongeRewardHandler, SpongeSubmissions> {

    public Path getConfigDir() {
        return configDir;
    }

    private final Path configDir;
    private final PluginContainer pluginContainer;

    public static SpongeServerSaturday getPlugin() {
        return Sponge.pluginManager().plugin("serversaturday").map(PluginContainer::instance).filter(SpongeServerSaturday.class::isInstance).map(SpongeServerSaturday.class::cast).orElseThrow(() -> new IllegalStateException("ServerSaturday is not enabled!"));
    }

    @NotNull
    public Logger getLogger() {
        return pluginContainer.logger();
    }

    @Inject
    public SpongeServerSaturday(PluginContainer pluginContainer, @ConfigDir(sharedRoot = false) Path configDir) {
        this.pluginContainer = pluginContainer;
        this.configDir = configDir;
    }

    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }

    @Override
    public void reloadPluginConfig() {
        Path configFile = configDir.resolve("config.yml");
        if (Files.notExists(configDir)) {
            try {
                Files.createDirectories(configDir);
            }
            catch (IOException e) {
                getLogger().error("Failed to write default config!", e);
            }
        }

        if (Files.notExists(configFile)) {
            pluginContainer.openResource(URI.create("config.yml")).ifPresent(i -> {
                try {
                    Files.createFile(configFile);
                    Files.write(configFile, i.readAllBytes());
                }
                catch (IOException e) {
                    getLogger().error("Failed to write default config!", e);
                }
            });
        }

        try {
            ConfigurationNode config = YamlConfigurationLoader.builder().file(configFile.toFile()).build().load();
            this.config = new Config(config.getList(String.class, List.of()));
        }
        catch (ConfigurateException e) {
            getLogger().error("Failed to read config!");
        }
    }

    @Listener
    public void onServerStop(StoppingEngineEvent<Server> event) {
        getSubmissions().save();
        getRewardHandler().save();
    }

    @Listener
    public void onServerStart(StartingEngineEvent<Server> event) {
        reloadPluginConfig();
        getSubmissions().load();
        getRewardHandler().load();
    }

    @Listener
    public void registerCommands(RegisterCommandEvent<Parameterized> event) {
        CMDExecutor.register(event, pluginContainer, new SSCommand());
    }
}
