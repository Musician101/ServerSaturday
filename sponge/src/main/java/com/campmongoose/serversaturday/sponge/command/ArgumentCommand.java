package com.campmongoose.serversaturday.sponge.command;

import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParser;


public abstract class ArgumentCommand<T> {

    @NotNull
    public abstract ValueParser<T> parser();

    @NotNull
    public abstract TypeToken<T> getType();

    @NotNull
    protected abstract String name();

    @NotNull
    protected Parameter.Value<T> toParameter() {
        return Parameter.builder(getType()).key(name()).addParser(parser()).build();
    }
}
