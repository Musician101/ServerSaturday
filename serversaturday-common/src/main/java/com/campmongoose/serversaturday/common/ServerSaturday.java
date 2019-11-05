package com.campmongoose.serversaturday.common;

import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submissions;
import com.campmongoose.serversaturday.common.submission.Submitter;
import javax.annotation.Nonnull;

public interface ServerSaturday<B extends Build<B, I, O, U, T>, I, J, O, P, R extends RewardGiver<J, P>, S extends Submissions<P, U>, T, U extends Submitter<B, I, O, U, T>> {

    @Nonnull
    String getId();

    @Nonnull
    R getRewardGiver();

    @Nonnull
    S getSubmissions();
}
