package com.campmongoose.serversaturday.common;

import com.campmongoose.serversaturday.common.submission.Submitter;
import io.musician101.musicianlibrary.java.storage.DataStorage;
import javax.annotation.Nonnull;

public interface ServerSaturday<R extends RewardGiver<?, ?>, T> {

    @Nonnull
    R getRewardGiver();

    @Nonnull
    DataStorage<?, Submitter<T>> getSubmissions();
}
