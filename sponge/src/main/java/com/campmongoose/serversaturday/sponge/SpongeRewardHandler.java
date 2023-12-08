package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.RewardHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import static com.campmongoose.serversaturday.sponge.SpongeServerSaturday.getPlugin;

public final class SpongeRewardHandler extends RewardHandler<ServerPlayer> {

    private final Path configFile = getPlugin().getConfigDir().resolve("rewards.yml");
    private final YamlConfigurationLoader loader = YamlConfigurationLoader.builder().file(configFile.toFile()).build();

    private void setUpFiles() throws IOException {
        if (Files.notExists(configFile.getParent())) {
            Files.createDirectories(configFile.getParent());
        }

        if (Files.notExists(configFile)) {
            Files.createFile(configFile);
        }
    }

    @Override
    public void claimReward(@NotNull ServerPlayer player) {
        UUID uuid = player.uniqueId();
        int amount = rewards.getOrDefault(uuid, 0);
        rewards.put(uuid, 0);
        IntStream.range(0, amount).forEach(i -> getPlugin().getPluginConfig().getRewards().forEach(command -> {
            try {
                Sponge.server().commandManager().process(command.replace("@p", player.name()));
            }
            catch (CommandException e) {
                player.sendMessage(Component.text("One or more rewards failed to be given. Please contact a server admin."));
                getPlugin().getLogger().error("One or more rewards failed to be given. Please contact a server admin.", e);
            }
        }));
    }

    @Override
    public void load() {
        SpongeServerSaturday plugin = getPlugin();
        try {
            setUpFiles();
            ConfigurationNode rewards = loader.load();
            rewards.childrenMap().forEach((key, node) -> {
                String k = key.toString();
                this.rewards.put(UUID.fromString(k), node.getInt());
            });
        }
        catch (Exception e) {
            plugin.getLogger().error(Messages.failedToReadFile(configFile), e);
        }
    }

    @Listener
    public void onJoin(@NotNull ServerSideConnectionEvent.Join event) {
        ServerPlayer player = event.player();
        UUID uuid = player.uniqueId();
        if (rewards.getOrDefault(uuid, 0) > 0) {
            Task task = Task.builder().plugin(getPlugin().getPluginContainer()).delay(1, TimeUnit.SECONDS).execute(() -> player.sendMessage(Messages.REWARDS_WAITING)).build();
            Sponge.asyncScheduler().submit(task, player.name() + " rewards waiting message task");
        }
    }

    public void save() {
        SpongeServerSaturday plugin = getPlugin();
        Path path = plugin.getConfigDir().resolve("rewards.yml");
        try {
            if (Files.notExists(plugin.getConfigDir())) {
                Files.createDirectories(plugin.getConfigDir());
            }

            if (Files.notExists(path)) {
                Files.createFile(path);
            }

            ConfigurationNode rewards = BasicConfigurationNode.root();
            this.rewards.forEach((uuid, i) -> {
                try {
                    rewards.node(uuid.toString()).set(i);
                }
                catch (SerializationException e) {
                    plugin.getLogger().error(Messages.failedToWriteFile(path), e);
                }
            });
            loader.save(rewards);
        }
        catch (Exception e) {
            plugin.getLogger().error(Messages.failedToWriteFile(path), e);
        }
    }
}
