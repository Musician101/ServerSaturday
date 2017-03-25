package com.campmongoose.serversaturday.spigot.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SpigotCommandUsage {

    private final int minArgs;
    @Nonnull
    private final String usage;

    public SpigotCommandUsage(@Nonnull List<SpigotCommandArgument> arguments) {
        this(arguments, 0);
    }

    public SpigotCommandUsage(@Nonnull List<SpigotCommandArgument> arguments, int minArgs) {
        this.minArgs = minArgs;
        this.usage = parseUsage(arguments);
    }

    private static String parseUsage(List<SpigotCommandArgument> arguments) {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.GRAY).append(arguments.get(0).getFormattedArgument());
        if (arguments.size() > 1) {
            for (int x = 1; x < arguments.size(); x++) {
                sb.append(" ").append(ChatColor.RESET).append(arguments.get(x).getFormattedArgument());
            }
        }

        return sb.toString();
    }

    @Nonnull
    public String getUsage() {
        return usage;
    }

    public boolean minArgsMet(@Nonnull CommandSender sender, int argsLength) {
        if (argsLength >= minArgs) {
            return true;
        }

        sender.sendMessage(ChatColor.RED + Messages.NOT_ENOUGH_ARGS);
        sender.sendMessage(usage);
        return false;
    }
}
