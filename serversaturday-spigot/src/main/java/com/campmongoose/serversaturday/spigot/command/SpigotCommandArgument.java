package com.campmongoose.serversaturday.spigot.command;

import com.campmongoose.serversaturday.common.command.AbstractCommandArgument;
import com.campmongoose.serversaturday.common.command.Syntax;
import java.util.Arrays;
import org.bukkit.ChatColor;

public class SpigotCommandArgument extends AbstractCommandArgument<String> {

    public SpigotCommandArgument(String name) {
        this(name, Syntax.LITERAL);
    }

    public SpigotCommandArgument(String name, Syntax... syntaxArray) {
        super(name, Arrays.asList(syntaxArray), (arg, syntaxList) -> {
            if (syntaxList.contains(Syntax.REPLACE)) {
                arg = ChatColor.ITALIC + arg;
            }

            if (syntaxList.contains(Syntax.MULTIPLE)) {
                arg = arg + "...";
            }

            if (syntaxList.contains(Syntax.OPTIONAL)) {
                arg = "[" + arg + "]";
            }

            if (syntaxList.contains(Syntax.REQUIRED)) {
                arg = "<" + arg + ">";
            }

            return arg;
        });
    }
}
