package com.campmongoose.serversaturday.common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public abstract class RewardHandler<P> {

    @NotNull
    protected final Map<UUID, Integer> rewards = new HashMap<>();

    public abstract void claimReward(@NotNull P player);

    public void giveReward(@NotNull UUID player) {
        rewards.compute(player, (uuid, i) -> i == null ? 1 : ++i);
    }

    public abstract void load();

    public abstract void save();
}
