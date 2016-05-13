package com.campmongoose.serversaturday.spigot.command;

import com.campmongoose.serversaturday.spigot.ServerSaturday;
import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class SpigotHelpCommand extends AbstractSpigotCommand
{
    private final AbstractSpigotCommand mainCommand;

    public SpigotHelpCommand(AbstractSpigotCommand mainCommand)
    {
        super(Commands.HELP_NAME, Commands.HELP_DESC_PREFIX + ChatColor.stripColor(mainCommand.getUsage().getUsage()), new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(ChatColor.stripColor(mainCommand.getUsage().getUsage())), new SpigotCommandArgument(Commands.HELP_NAME)), 1), new SpigotCommandPermissions("", false));
        this.mainCommand = mainCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)
    {
        sender.sendMessage(ChatColor.GREEN + "===== " + ChatColor.RESET + Reference.NAME + " v" + ServerSaturday.getInstance().getDescription().getVersion() + ChatColor.GREEN + " by " + ChatColor.RESET + "Bruce" + ChatColor.GREEN + " =====");
        sender.sendMessage(mainCommand.getCommandHelpInfo());
        mainCommand.getSubCommands().stream().filter(command -> sender.hasPermission(command.getPermissions().getPermissionNode())).forEach(command -> sender.sendMessage(command.getCommandHelpInfo()));
        return true;
    }
}
