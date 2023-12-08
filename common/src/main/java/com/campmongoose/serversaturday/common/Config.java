package com.campmongoose.serversaturday.common;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class Config {

    @NotNull
    private final List<String> rewards;

    public Config() {
        this(List.of("tell @p Quack \\_o<"));
    }

    public Config(@NotNull List<String> rewards) {
        this.rewards = rewards;
    }

    @NotNull
    public List<String> getRewards() {
        return new ArrayList<>(rewards);
    }
}
