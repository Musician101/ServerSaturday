package com.campmongoose.serversaturday.command;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class CommandArgument
{
    private final List<Syntax> syntaxList;
    private String name;

    public CommandArgument(String name)
    {
        this(name, Syntax.LITERAL);
    }

    public CommandArgument(String name, Syntax... syntaxes)
    {
        this.syntaxList = Arrays.asList(syntaxes);
        if (syntaxList.contains(Syntax.REQUIRED) && syntaxList.contains(Syntax.OPTIONAL))
            throw new IllegalArgumentException("Common arguments cannot be both Optional and Required.");

        this.name = name;
    }

    public String format()
    {
        String name = this.name;
        if (syntaxList.contains(Syntax.MULTIPLE))
            name = name + "...";

        if (syntaxList.contains(Syntax.REPLACE))
            name = ChatColor.ITALIC + name;

        if (syntaxList.contains(Syntax.OPTIONAL))
            name = "[" + name + "]";

        if (syntaxList.contains(Syntax.REQUIRED))
            name = "<" + name + ">";

        return name;
    }

    public enum Syntax
    {
        LITERAL,
        MULTIPLE,
        REPLACE,
        REQUIRED,
        OPTIONAL
    }
}
