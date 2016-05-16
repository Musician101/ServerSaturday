package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.command.AbstractCommand;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractSpongeCommand extends AbstractCommand<Text, SpongeCommandUsage, SpongeCommandPermissions, AbstractSpongeCommand, CommandSource> implements CommandCallable
{
    protected AbstractSpongeCommand(String name, String description, SpongeCommandUsage usage, SpongeCommandPermissions permissions)
    {
        super(name, Text.of(description), usage, permissions, new ArrayList<>());
    }

    @SuppressWarnings("SameParameterValue")
    protected AbstractSpongeCommand(String name, String description, SpongeCommandUsage usage, SpongeCommandPermissions permissions, List<AbstractSpongeCommand> subCommands)
    {
        super(name, Text.of(description), usage, permissions, subCommands);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @Override
    protected boolean minArgsMet(CommandSource source, int args)
    {
        if (args >= usage.getMinArgs())
            return true;

        source.sendMessage(Text.builder(Messages.NOT_ENOUGH_ARGS).color(TextColors.RED).build());
        source.sendMessage(usage.getUsage());
        return false;
    }

    @Override
    public boolean testPermission(@Nonnull CommandSource source)
    {
        if (permissions.isPlayerOnly() && !(source instanceof Player))
        {
            source.sendMessage(permissions.getPlayerOnly());
            return false;
        }

        if (!source.hasPermission(permissions.getPermissionNode()))
        {
            source.sendMessage(permissions.getNoPermission());
            return false;
        }

        return true;
    }

    @Nonnull
    @Override
    public List<String> getSuggestions(@Nonnull CommandSource source, @Nonnull String arguments)
    {
        String[] args = splitArgs(arguments);
        List<String> list = new ArrayList<>();
        for (AbstractSpongeCommand command : subCommands)
        {
            if (args.length == 0)
                list.add(command.getName());

            if (command.getName().startsWith(args[args.length - 1]))
                list.add(command.getName());
        }

        return list;
    }

    @Nonnull
    @Override
    public Optional<? extends Text> getHelp(@Nonnull CommandSource source)
    {
        return Optional.of(Text.join(Text.of(" "), usage.getUsage(), description));
    }

    @Nonnull
    @Override
    public Optional<? extends Text> getShortDescription(@Nonnull CommandSource source)
    {
        return Optional.of(description);
    }

    @Nonnull
    @Override
    public Text getUsage(@Nonnull CommandSource source)
    {
        return usage.getUsage();
    }

    protected String moveArguments(String[] args)
    {
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>();
        Collections.addAll(list, args);
        if (!list.isEmpty())
            list.remove(0);

        for (String arg : list)
        {
            if (sb.length() == 0)
                sb.append(arg);
            else
                sb.append(" ").append(arg);
        }

        return sb.toString();
    }

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

    protected String[] splitArgs(String arguments)
    {
        return arguments.split("\\s");
    }
}
