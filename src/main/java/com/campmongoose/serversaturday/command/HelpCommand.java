package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.ServerSaturday;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class HelpCommand extends AbstractCommand
{
    private final AbstractCommand mainCommand;

    public HelpCommand(AbstractCommand mainCommand)
    {
        super("help", "Display help info for " + ChatColor.stripColor(mainCommand.getUsage()), Arrays.asList(new CommandArgument(ChatColor.stripColor(mainCommand.getUsage())), new CommandArgument("help")), 1, "", false);
        this.mainCommand = mainCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)
    {
        ServerSaturday plugin = ServerSaturday.instance();
        sender.sendMessage(ChatColor.GREEN + "===== " + ChatColor.RESET + plugin.getName() + " v" + plugin.getDescription().getVersion() + ChatColor.GREEN + " by " + ChatColor.RESET + "Bruce" + ChatColor.GREEN + " =====");
        sender.sendMessage(mainCommand.getCommandHelpInfo());
        mainCommand.getSubCommands().stream().filter(command -> sender.hasPermission(command.getPermission())).forEach(command -> sender.sendMessage(command.getCommandHelpInfo()));
        return true;
    }
}