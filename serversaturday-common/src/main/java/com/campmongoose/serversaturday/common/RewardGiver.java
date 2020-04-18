package com.campmongoose.serversaturday.common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;

public abstract class RewardGiver<J, P> {

    @Nonnull
    protected final File file;
    @Nonnull
    protected final Map<UUID, Integer> rewardsWaiting = new HashMap<>();

    protected RewardGiver(@Nonnull File file) {
        this.file = file;
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

    public abstract void save();
}
