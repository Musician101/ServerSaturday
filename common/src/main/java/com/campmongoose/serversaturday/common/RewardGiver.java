package com.campmongoose.serversaturday.common;

import io.leangen.geantyref.TypeToken;
import io.musician101.musicianlibrary.java.configurate.ConfigurateLoader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

public abstract class RewardGiver<J, P> {

    @Nonnull
    protected final File file;
    @Nonnull
    protected final Map<UUID, Integer> rewardsWaiting = new HashMap<>();
    @Nullable
    private ConfigurationLoader<? extends ConfigurationNode> loader;

    @SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
    protected RewardGiver(@Nonnull File file, @Nonnull ConfigurateLoader configurateLoader) {
        this.file = file;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            loader = configurateLoader.loader(file.toPath());
            ConfigurationNode node = loader.load();
            rewardsWaiting.putAll(node.get(new TypeToken<Map<UUID, Integer>>() {

            }));
        }
        catch (IOException e) {
            reportError();
        }
    }

    public void addReward(@Nonnull UUID uuid) {
        if (rewardsWaiting.containsKey(uuid)) {
            rewardsWaiting.compute(uuid, (id, amount) -> amount == null ? 1 : ++amount);
            return;
        }

        rewardsWaiting.put(uuid, 1);
    }

    public abstract void givePlayerReward(@Nonnull P player);

    public abstract void onJoin(@Nonnull J event);

    protected abstract void reportError();

    public void save() {
        try {
            ConfigurationNode node = BasicConfigurationNode.root();
            node.set(rewardsWaiting);
            if (loader != null) {
                loader.save(node);
                return;
            }
        }
        catch (ConfigurateException ignored) {

        }

        reportError();
    }
}
