package com.campmongoose.serversaturday.common.command;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;

public abstract class AbstractCommandArgument<M> {

    @Nonnull
    private BiFunction<String, List<Syntax>, M> formatter;
    @Nonnull
    private String name;
    @Nonnull
    private List<Syntax> syntaxList;

    public AbstractCommandArgument(@Nonnull String name, @Nonnull BiFunction<String, List<Syntax>, M> formatter) {
        this(name, Collections.singletonList(Syntax.LITERAL), formatter);
    }

    public AbstractCommandArgument(@Nonnull String name, @Nonnull List<Syntax> syntaxList, @Nonnull BiFunction<String, List<Syntax>, M> formatter) {
        this.name = name;
        this.syntaxList = syntaxList;
        this.formatter = formatter;
    }

    @Nonnull
    public M getFormattedArgument() {
        return formatter.apply(name, syntaxList);
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public List<Syntax> getSyntaxList() {
        return syntaxList;
    }
}
