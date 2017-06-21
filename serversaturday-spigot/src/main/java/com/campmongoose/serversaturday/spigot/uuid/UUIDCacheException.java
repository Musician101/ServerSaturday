package com.campmongoose.serversaturday.spigot.uuid;

import com.campmongoose.serversaturday.common.Reference.Messages;

public class UUIDCacheException extends Exception {

    public UUIDCacheException() {
        super(Messages.PREFIX + "Local UUID Cache has not finished initialization.");
    }
}
