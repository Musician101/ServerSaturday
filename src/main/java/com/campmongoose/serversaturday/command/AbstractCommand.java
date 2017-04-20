package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Submissions;
import com.campmongoose.serversaturday.submission.Submitter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommand
{
    private final boolean isPlayerOnly;
    private final int minArgs;
    private final List<AbstractCommand> subCommands;
    private final String description;
    private final String name;
    private final String permission;
    private final String usage;

    protected AbstractCommand(String name, String description, List<CommandArgument> usage, int minArgs, String permission, boolean isPlayerOnly)
    {
        this(name, description, usage, minArgs, permission, isPlayerOnly, new ArrayList<>());
    }

    protected AbstractCommand(String name, String description, List<CommandArgument> usage, int minArgs, String permission, boolean isPlayerOnly, List<AbstractCommand> subCommands)
    {
        this.name = name;
        this.description = description;
        this.usage = parseUsage(usage);
        this.minArgs = minArgs;
        this.isPlayerOnly = isPlayerOnly;
        this.permission = permission;
        this.subCommands = subCommands;
    }

    private String parseUsage(List<CommandArgument> usageList)
    {
        String usage = ChatColor.GRAY + usageList.get(0).format();
        if (usageList.size() > 1)
            usage += " " + ChatColor.RESET + usageList.get(1).format();

        if (usageList.size() > 2)
            for (int x = 2; x < usageList.size(); x++)
                usage += " " + ChatColor.GREEN + usageList.get(x).format();

        return usage;
    }

    public abstract boolean onCommand(CommandSender sender, String... args);

    protected boolean canSenderUseCommand(CommandSender sender)
    {
        if (isPlayerOnly() && !(sender instanceof Player))
        {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return false;
        }

        if (!sender.hasPermission(permission))
        {
            sender.sendMessage(Messages.NO_PERMISSION);
            return false;
        }

        return true;
    }

    private boolean isPlayerOnly()
    {
        return isPlayerOnly;
    }

    protected boolean minArgsMet(CommandSender sender, int args)
    {
        if (args >= getMinArgs())
            return true;

        sender.sendMessage(Messages.NOT_ENOUGH_ARGS);
        sender.sendMessage(usage);
        return false;
    }

    private int getMinArgs()
    {
        return minArgs;
    }

    protected List<AbstractCommand> getSubCommands()
    {
        return subCommands;
    }

    private String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }

    String getUsage()
    {
        return usage;
    }

    String getCommandHelpInfo()
    {
        return getUsage() + " " + ChatColor.AQUA + getDescription();
    }

    String getPermission()
    {
        return permission;
    }

    protected String[] moveArguments(String[] args)
    {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, args);
        if (list.size() > 0)
            list.remove(0);

        return list.toArray(new String[list.size()]);
    }

    @Deprecated
    protected String combineStringArray(String[] stringArray)
    {
        StringBuilder sb = new StringBuilder();
        for (String part : stringArray)
        {
            if (sb.length() > 0)
                sb.append(" ");

            sb.append(part);
        }

        return sb.toString();
    }

    protected ServerSaturday getPluginInstance() {
        return ServerSaturday.instance();
    }

    protected Submissions getSubmissions() {
        return getPluginInstance().getSubmissions();
    }

    @Nonnull
    protected Submitter getSubmitter(Player player) {
        return getSubmissions().getSubmitter(player.getUniqueId());
    }

    @Nullable
    protected Submitter getSubmitter(String playerName) {
        UUID uuid = getPluginInstance().getUUIDCache().getUUIDOf(name);
        if (uuid != null) {
            Submitter submitter = getSubmitter(uuid);
            if (submitter != null) {
                return submitter;
            }

            for (Submitter s : getSubmissions().getSubmitters()) {
                if (s.getName().equalsIgnoreCase(playerName)) {
                    return s;
                }
            }
        }

        return null;
    }

    @Nullable
    protected Submitter getSubmitter(UUID uuid) {
        return getSubmissions().getSubmitter(uuid);
    }
}
