package com.campmongoose.serversaturday.common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class RewardGiver<J, P> {

    protected final File file;
    protected final Map<UUID, Integer> rewardsWaiting = new HashMap<>();

    protected RewardGiver(File file) {
        this.file = file;
    }

    public void addReward(UUID uuid) {
        rewardsWaiting.compute(uuid, (id, amount) -> ++amount);
    }

    public abstract void givePlayerReward(P player);

    public abstract void onJoin(J event);

    public abstract void save();
}
