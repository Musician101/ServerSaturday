package com.campmongoose.serversaturday.spigot.command;

import com.campmongoose.serversaturday.common.AbstractCommand;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.uuid.UUIDUtils;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmissions;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractSpigotCommand extends AbstractCommand<SpigotBuild, SpigotServerSaturday, Location, Player, SpigotSubmitter, SpigotSubmissions, ItemStack> {

    private final String description;
    private final String name;
    private final SpigotCommandPermissions permissions;
    private final List<AbstractSpigotCommand> subCommands;
    private final SpigotCommandUsage usage;

    public AbstractSpigotCommand(String name, String description, SpigotCommandUsage usage, SpigotCommandPermissions permissions) {
        this(name, description, usage, permissions, new ArrayList<>());
    }

    public AbstractSpigotCommand(String name, String description, SpigotCommandUsage usage, SpigotCommandPermissions permissions, List<AbstractSpigotCommand> subCommands) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.permissions = permissions;
        this.subCommands = subCommands;
    }

    protected String combineStringArray(String[] stringArray) {
        StringBuilder sb = new StringBuilder();
        for (String part : stringArray) {
            if (sb.length() > 0) {
                sb.append(" ");
            }

            sb.append(part);
        }

        return sb.toString();
    }

    public String getCommandHelpInfo() {
        return getUsage() + " " + ChatColor.AQUA + getDescription();
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public SpigotCommandPermissions getPermissions() {
        return permissions;
    }

    @Nonnull
    @Override
    protected SpigotServerSaturday getPluginInstance() {
        return SpigotServerSaturday.instance();
    }

    public List<AbstractSpigotCommand> getSubCommands() {
        return subCommands;
    }

    @Nonnull
    @Override
    protected SpigotSubmissions getSubmissions() {
        return getPluginInstance().getSubmissions();
    }

    @Nonnull
    @Override
    protected SpigotSubmitter getSubmitter(Player player) {
        return getSubmissions().getSubmitter(player);
    }

    @Nullable
    @Override
    protected SpigotSubmitter getSubmitter(String playerName) {
        try {
            return getSubmitter(UUIDUtils.getUUIDOf(playerName));
        }
        catch (IOException e) {
            for (SpigotSubmitter s : getSubmissions().getSubmitters()) {
                if (s.getName().equalsIgnoreCase(playerName)) {
                    return s;
                }
            }
        }

        return null;
    }

    @Nullable
    protected SpigotSubmitter getSubmitter(UUID uuid) {
        return getSubmissions().getSubmitter(uuid);
    }

    public SpigotCommandUsage getUsage() {
        return usage;
    }

    protected boolean minArgsMet(CommandSender sender, int args) {
        if (args >= usage.getMinArgs()) {
            return true;
        }

        sender.sendMessage(ChatColor.RED + Messages.NOT_ENOUGH_ARGS);
        sender.sendMessage(usage.getUsage());
        return false;
    }

    protected String[] moveArguments(String[] args) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, args);
        if (!list.isEmpty()) {
            list.remove(0);
        }

        return list.toArray(new String[list.size()]);
    }

    public abstract boolean onCommand(CommandSender sender, String... args);

    protected boolean testPermission(CommandSender sender) {
        if (permissions.isPlayerOnly() && !(sender instanceof Player)) {
            sender.sendMessage(permissions.getNoPermission());
            return false;
        }

        if (!sender.hasPermission(permissions.getPermissionNode())) {
            sender.sendMessage(permissions.getPlayerOnly());
            return false;
        }

        return true;
    }
}
