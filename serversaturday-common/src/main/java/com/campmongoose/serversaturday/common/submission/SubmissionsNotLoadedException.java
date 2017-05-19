package com.campmongoose.serversaturday.common.submission;

import com.campmongoose.serversaturday.common.Reference.Messages;

public class SubmissionsNotLoadedException extends Exception {

    public SubmissionsNotLoadedException() {
        super(Messages.PREFIX + "Submissions have not loaded.");
    }
}
