package com.campmongoose.serversaturday.spigot.command;

import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;

public class SpigotCommandUsage {

    @Nonnull
    private final String usage;
    private int minArgs = 0;

    public SpigotCommandUsage(@Nonnull List<SpigotCommandArgument> arguments) {
        this(arguments, 0);
    }

    public SpigotCommandUsage(List<SpigotCommandArgument> arguments, int minArgs) {
        this.usage = parseUsage(arguments);
        this.minArgs = minArgs;
    }

    public int getMinArgs() {
        return minArgs;
    }

    @Nonnull
    public String getUsage() {
        return usage;
    }

    private String parseUsage(List<SpigotCommandArgument> arguments) {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.GRAY).append(arguments.get(0).format());
        if (arguments.size() > 1) {
            sb.append(" ").append(ChatColor.RESET).append(arguments.get(1).format());
        }

        if (arguments.size() > 2) {
            for (int x = 2; x < arguments.size() - 1; x++) {
                sb.append(" ").append(ChatColor.GREEN).append(arguments.get(x).format());
            }
        }

        return sb.toString();
    }
}
