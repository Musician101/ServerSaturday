package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class SpigotHelpCommand extends AbstractSpongeCommand
{
    private final AbstractSpongeCommand mainCommand;

    public SpigotHelpCommand(AbstractSpongeCommand mainCommand)
    {
        super(Commands.HELP_NAME, Commands.HELP_DESC_PREFIX + mainCommand.getUsage().getUsage().toPlain(), new SpongeCommandUsage(Arrays.asList(new SpongeCommandArgument(mainCommand.getUsage().getUsage().toPlain()), new SpongeCommandArgument(Commands.HELP_NAME)), 1), new SpongeCommandPermissions("", false));
        this.mainCommand = mainCommand;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Nonnull
    @Override
    public CommandResult process(@Nonnull CommandSource source, @Nonnull String arguments)
    {
        source.sendMessage(Text.join(Text.builder("===== ").color(TextColors.GREEN).build(), Text.of(Reference.NAME + " v" + Reference.DESCRIPTION), Text.builder(" by ").color(TextColors.GREEN).build(), Text.of("Bruce"), Text.builder(" =====").color(TextColors.GREEN).build()));
        source.sendMessage(mainCommand.getHelp(source).get());
        mainCommand.getSubCommands().stream().filter(command -> source.hasPermission(command.getPermissions().getPermissionNode())).forEach(command -> source.sendMessage(command.getHelp(source).get()));
        return CommandResult.success();
    }
}
