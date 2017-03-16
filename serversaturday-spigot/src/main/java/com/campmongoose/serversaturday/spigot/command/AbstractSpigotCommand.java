package com.campmongoose.serversaturday.spigot.command;

import com.campmongoose.serversaturday.common.command.AbstractCommand;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmissions;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

//TODO rewrite commands so that they're separate from each other. Add aliases when accomplishing this
public abstract class AbstractSpigotCommand extends AbstractCommand<SpigotCommandArgument, SpigotBuild, AbstractSpigotCommand, SpigotServerSaturday, Location, String, Player, SpigotSubmitter, Boolean, SpigotSubmissions, ItemStack, SpigotCommandUsage, SpigotCommandPermissions, CommandSender> implements CommandExecutor {

    public AbstractSpigotCommand(String name, String description) {
        super(name, description);
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

    /**
     * @deprecated placeholder until commands are all separated
     * @param sender
     * @param args
     * @return
     */
    @Deprecated
    public boolean onCommand(CommandSender sender, List<String> args) {
        if (permissions.testPermission(sender)) {
            if (usage.minArgsMet(sender, args.size())) {
                return executor.apply(sender, args);
            }
        }

        return false;
    }

    @Override
    public AbstractSpigotCommand getHelpCommand() {
        return new SpigotHelpCommand(this);
    }

    /**
     * @deprecated Replace with org.apache.commons.lang.StringUtils.join(array, separator)
     * @param stringArray
     * @return
     */
    @Deprecated
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

    @Nonnull
    @Override
    public String getHelp() {
        return usage.getUsage() + " " + ChatColor.AQUA + description;
    }

    @Nonnull
    @Override
    protected SpigotServerSaturday getPluginInstance() {
        return SpigotServerSaturday.instance();
    }

    @Nonnull
    @Override
    protected SpigotSubmissions getSubmissions() {
        return getPluginInstance().getSubmissions();
    }

    @Nonnull
    @Override
    protected SpigotSubmitter getSubmitter(@Nonnull Player player) {
        return getSubmissions().getSubmitter(player);
    }

    @Nullable
    @Override
    protected SpigotSubmitter getSubmitter(@Nonnull String playerName) {
        UUID uuid = getPluginInstance().getUUIDCache().getUUIDOf(playerName);
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
}
