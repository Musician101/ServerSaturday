package com.campmongoose.serversaturday.forge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import java.util.List;
import javax.annotation.Nonnull;

public class ForgeCommandUsage {

    private final int minArgs;
    @Nonnull
    private final String usage;

    public ForgeCommandUsage(@Nonnull List<ForgeCommandArgument> arguments) {
        this(arguments, 0);
    }

    public ForgeCommandUsage(@Nonnull List<ForgeCommandArgument> arguments, int minArgs) {
        this.minArgs = minArgs;
        this.usage = parseUsage(arguments);
    }

    private static String parseUsage(List<ForgeCommandArgument> arguments) {
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

    public boolean minArgsMet(@Nonnull CommandSource source, int argsLength) {
        if (argsLength >= minArgs) {
            return true;
        }

        source.sendFeedback(ChatColor.RED + Messages.NOT_ENOUGH_ARGS);
        source.sendFeedback(usage);
        return false;
    }
}
