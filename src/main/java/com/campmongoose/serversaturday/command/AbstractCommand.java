package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Submissions;
import com.campmongoose.serversaturday.submission.Submitter;
import com.campmongoose.serversaturday.util.MojangAPIException;
import com.campmongoose.serversaturday.util.PlayerNotFoundException;
import com.campmongoose.serversaturday.util.UUIDUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommand {

    private final String description;
    private final boolean isPlayerOnly;
    private final int minArgs;
    private final String name;
    private final String permission;
    private final List<AbstractCommand> subCommands;
    private final String usage;

    protected AbstractCommand(String name, String description, List<CommandArgument> usage, int minArgs, String permission, boolean isPlayerOnly) {
        this(name, description, usage, minArgs, permission, isPlayerOnly, new ArrayList<>());
    }

    protected AbstractCommand(String name, String description, List<CommandArgument> usage, int minArgs, String permission, boolean isPlayerOnly, List<AbstractCommand> subCommands) {
        this.name = name;
        this.description = description;
        this.usage = parseUsage(usage);
        this.minArgs = minArgs;
        this.isPlayerOnly = isPlayerOnly;
        this.permission = permission;
        this.subCommands = subCommands;
    }

    protected boolean canSenderUseCommand(CommandSender sender) {
        if (isPlayerOnly() && !(sender instanceof Player)) {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return false;
        }

        if (!sender.hasPermission(permission)) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return false;
        }

        return true;
    }

    String getCommandHelpInfo() {
        return getUsage() + " " + ChatColor.AQUA + getDescription();
    }

    private String getDescription() {
        return description;
    }

    private int getMinArgs() {
        return minArgs;
    }

    public String getName() {
        return name;
    }

    String getPermission() {
        return permission;
    }

    protected ServerSaturday getPluginInstance() {
        return ServerSaturday.instance();
    }

    protected List<AbstractCommand> getSubCommands() {
        return subCommands;
    }

    @Nonnull
    protected Submissions getSubmissions() {
        return getPluginInstance().getSubmissions();
    }

    @Nullable
    protected Submitter getSubmitter(Player player) {
        return getSubmissions().getSubmitter(player.getUniqueId());
    }

    @Nullable
    protected Submitter getSubmitter(String playerName) throws MojangAPIException, IOException, PlayerNotFoundException {
        UUID uuid = UUIDUtils.getUUIDOf(playerName);
        Submitter submitter = getSubmitter(uuid);
        if (submitter != null) {
            return submitter;
        }

        for (Submitter s : getSubmissions().getSubmitters()) {
            if (s.getName().equalsIgnoreCase(playerName)) {
                return s;
            }
        }

        return null;
    }

    @Nullable
    protected Submitter getSubmitter(UUID uuid) {
        return getSubmissions().getSubmitter(uuid);
    }

    String getUsage() {
        return usage;
    }

    private boolean isPlayerOnly() {
        return isPlayerOnly;
    }

    protected boolean minArgsMet(CommandSender sender, int args) {
        if (args >= getMinArgs()) {
            return true;
        }

        sender.sendMessage(Messages.NOT_ENOUGH_ARGS);
        sender.sendMessage(usage);
        return false;
    }

    protected String[] moveArguments(String[] args) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, args);
        if (list.size() > 0) {
            list.remove(0);
        }

        return list.toArray(new String[list.size()]);
    }

    public abstract boolean onCommand(CommandSender sender, String... args);

    private String parseUsage(List<CommandArgument> usageList) {
        StringBuilder usage = new StringBuilder();
        usage.append(ChatColor.GRAY).append(usageList.get(0).format());
        if (usageList.size() > 1) {
            usage.append(" ").append(ChatColor.RESET).append(usageList.get(1).format());
        }

        if (usageList.size() > 2) {
            for (int x = 2; x < usageList.size(); x++) {
                usage.append(" ").append(ChatColor.GREEN).append(usageList.get(x).format());
            }
        }

        return usage.toString();
    }
}
