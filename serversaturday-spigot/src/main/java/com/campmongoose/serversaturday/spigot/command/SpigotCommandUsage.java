package com.campmongoose.serversaturday.spigot.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.command.AbstractCommandUsage;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SpigotCommandUsage extends AbstractCommandUsage<SpigotCommandArgument, String, CommandSender> {

    public SpigotCommandUsage(@Nonnull List<SpigotCommandArgument> arguments) {
        this(arguments, 0);
    }

    public SpigotCommandUsage(List<SpigotCommandArgument> arguments, int minArgs) {
        super(minArgs, arguments, args -> {
            StringBuilder sb = new StringBuilder();
            sb.append(ChatColor.GRAY).append(arguments.get(0).getFormattedArgument());
            if (arguments.size() > 1) {
                sb.append(" ").append(ChatColor.RESET).append(arguments.get(1).getFormattedArgument());
            }

            if (arguments.size() > 2) {
                for (int x = 2; x < arguments.size() - 1; x++) {
                    sb.append(" ").append(ChatColor.GREEN).append(arguments.get(x).getFormattedArgument());
                }
            }

            return sb.toString();
        });

        minArgsChecker = (sender, argsLength) -> {
            if (argsLength >= minArgs) {
                return true;
            }

            sender.sendMessage(ChatColor.RED + Messages.NOT_ENOUGH_ARGS);
            sender.sendMessage(getUsage());
            return false;
        };
    }
}
