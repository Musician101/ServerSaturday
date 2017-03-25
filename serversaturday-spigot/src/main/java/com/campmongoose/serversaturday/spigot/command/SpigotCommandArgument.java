package com.campmongoose.serversaturday.spigot.command;

import com.campmongoose.serversaturday.common.command.Syntax;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;

public class SpigotCommandArgument {

    @Nonnull
    private String name;
    @Nonnull
    private List<Syntax> syntaxList;

    public SpigotCommandArgument(@Nonnull String name) {
        this(name, Syntax.LITERAL);
    }

    public SpigotCommandArgument(@Nonnull String name, @Nonnull Syntax... syntaxArray) {
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

    @Nonnull
    public String getName() {
        return name;
    }
}
