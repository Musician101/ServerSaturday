package com.campmongoose.serversaturday.common.uuid;

import com.campmongoose.serversaturday.common.Reference.Messages;

public class MojangAPIException extends Exception {

    public MojangAPIException() {
        super(Messages.PREFIX + "An error occurred while pinging the MojangAPI.");
    }
}
