package com.campmongoose.serversaturday.spigot.command;

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;

public class SpigotCommandArgument
{
    private final List<Syntax> syntaxList;
    private final String name;

    public SpigotCommandArgument(String name)
    {
        this(name, Syntax.LITERAL);
    }

    public SpigotCommandArgument(String name, Syntax... syntaxes)
    {
        this.syntaxList = Arrays.asList(syntaxes);
        if (syntaxList.contains(Syntax.REQUIRED) && syntaxList.contains(Syntax.OPTIONAL))
            throw new IllegalArgumentException("Common arguments cannot be both Optional and Required.");

        this.name = name;
    }

    public String format()
    {
        String name = this.name;
        if (syntaxList.contains(Syntax.REPLACE))
            name = ChatColor.ITALIC + name;

        if (syntaxList.contains(Syntax.MULTIPLE))
            name = name + "...";

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
