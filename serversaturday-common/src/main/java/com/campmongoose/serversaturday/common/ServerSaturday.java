package com.campmongoose.serversaturday.common;

import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submissions;
import com.campmongoose.serversaturday.common.submission.Submitter;

public interface ServerSaturday<B extends Build<I, O, U>, I, J, L, O, P, R extends RewardGiver<J, P>, S extends Submissions<P, U>, U extends Submitter<B, I, O>> {

    String getId();

    L getLogger();

    R getRewardGiver();

    S getSubmissions();
}
