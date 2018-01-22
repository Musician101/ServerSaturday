package com.campmongoose.serversaturday.spigot.command;

import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmissions;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import com.campmongoose.serversaturday.spigot.uuid.UUIDUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SpigotCommand implements CommandExecutor {

    @Nonnull
    protected final String description;
    @Nonnull
    protected final String name;
    protected BiFunction<CommandSender, List<String>, Boolean> executor;
    protected SpigotCommandPermissions permissions;
    protected SpigotCommandUsage usage;

    public SpigotCommand(@Nonnull String name, @Nonnull String description) {
        this.name = name;
        this.description = description;
    }

    @Nonnull
    public String getDescription() {
        return description;
    }

    @Nonnull
    public String getHelp() {
        return usage.getUsage() + " " + ChatColor.AQUA + description;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public SpigotCommandPermissions getPermissions() {
        return permissions;
    }

    @Nonnull
    protected SpigotServerSaturday getPluginInstance() {
        return (SpigotServerSaturday) SpigotServerSaturday.instance();
    }

    @Nonnull
    protected SpigotSubmissions getSubmissions() {
        return getPluginInstance().getSubmissions();
    }

    @Nonnull
    protected SpigotSubmitter getSubmitter(@Nonnull Player player) {
        return getSubmissions().getSubmitter(player);
    }

    @Nullable
    protected SpigotSubmitter getSubmitter(@Nonnull String playerName) {
        UUID uuid;
        try {
            uuid = UUIDUtils.getUUIDOf(playerName);
        }
        catch (IOException e) {
            return null;
        }

        if (uuid != null) {
            SpigotSubmitter submitter = getSubmitter(uuid);
            if (submitter != null) {
                return submitter;
            }

            for (SpigotSubmitter s : getSubmissions().getSubmitters()) {
                if (s.getName().equalsIgnoreCase(playerName)) {
                    return s;
                }
            }
        }

        return null;
    }

    @Nullable
    protected SpigotSubmitter getSubmitter(@Nonnull UUID uuid) {
        return getSubmissions().getSubmitter(uuid);
    }

    @Nonnull
    public SpigotCommandUsage getUsage() {
        return usage;
    }

    protected List<String> moveArguments(List<String> args) {
        List<String> list = new ArrayList<>(args);
        if (!list.isEmpty()) {
            list.remove(0);
        }

        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (permissions.testPermission(sender)) {
            if (usage.minArgsMet(sender, args.length)) {
                return executor.apply(sender, Arrays.asList(args));
            }
        }

        return false;
    }
}