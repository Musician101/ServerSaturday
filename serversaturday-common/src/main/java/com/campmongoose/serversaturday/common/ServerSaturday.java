package com.campmongoose.serversaturday.common;

import com.campmongoose.serversaturday.common.submission.Submissions;
import javax.annotation.Nonnull;

public interface ServerSaturday<R extends RewardGiver<?, ?>, S extends Submissions<?, ?>> {

    @Nonnull
    R getRewardGiver();

    @Nonnull
    S getSubmissions();
}
