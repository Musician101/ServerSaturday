package com.campmongoose.serversaturday.spigot.command;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.command.sscommand.SSCommand;
import java.util.Arrays;
import org.bukkit.ChatColor;

/**
 * @deprecated Needs to be rewritten or dropped
 */
@Deprecated
public class SpigotHelpCommand extends AbstractSpigotCommand {

    private final AbstractSpigotCommand mainCommand;

    public SpigotHelpCommand(AbstractSpigotCommand mainCommand) {
        super(Commands.HELP_NAME, Commands.HELP_DESC_PREFIX + ChatColor.stripColor(mainCommand.getUsage().getUsage()));
        this.mainCommand = mainCommand;
    }

    @Override
    protected void build() {
        usage = new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(ChatColor.stripColor(mainCommand.getUsage().getUsage())), new SpigotCommandArgument(Commands.HELP_NAME)), 1);
        permissions = new SpigotCommandPermissions("", false);
        executor = (sender, args) -> {
            sender.sendMessage(ChatColor.GREEN + "===== " + ChatColor.RESET + Reference.NAME + " v" + SpigotServerSaturday.instance().getDescription().getVersion() + ChatColor.GREEN + " by " + ChatColor.RESET + "Bruce" + ChatColor.GREEN + " =====");
            sender.sendMessage(mainCommand.getHelp());
            if (mainCommand instanceof SSCommand) {
                ((SSCommand) mainCommand).getSubCommands().stream().filter(command -> sender.hasPermission(command.getPermissions().getPermissionNode())).forEach(command -> sender.sendMessage(command.getHelp()));
            }

            return true;
        };
    }
}
