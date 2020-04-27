package com.campmongoose.serversaturday.forge.command;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;

public class ForgeCommandArgument {

    @Nonnull
    private final List<Syntax> syntaxList;
    @Nonnull
    private String name;

    public ForgeCommandArgument(@Nonnull String name) {
        this(name, Syntax.LITERAL);
    }

    public ForgeCommandArgument(@Nonnull String name, @Nonnull Syntax... syntaxArray) {
        this.name = name;
        this.syntaxList = Arrays.asList(syntaxArray);
    }

    @Nonnull
    public String getFormattedArgument() {
        if (syntaxList.contains(Syntax.REPLACE)) {
            name = ChatColor.ITALIC + name + ChatColor.RESET;
        }

        if (syntaxList.contains(Syntax.MULTIPLE)) {
            name = name + "...";
        }

        if (syntaxList.contains(Syntax.OPTIONAL)) {
            name = "[" + name + "]";
        }

        if (syntaxList.contains(Syntax.REQUIRED)) {
            name = "<" + name + ">";
        }

        return name;
    }
}
