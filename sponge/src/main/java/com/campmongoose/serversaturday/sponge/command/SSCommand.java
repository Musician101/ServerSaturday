package com.campmongoose.serversaturday.sponge.command;

import io.musician101.spongecmd.CMDExecutor;
import io.musician101.spongecmd.help.HelpMainCMD;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;

import static com.campmongoose.serversaturday.sponge.SpongeServerSaturday.getPlugin;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;

public class SSCommand extends HelpMainCMD {

    public SSCommand() {
        super(getPlugin().getPluginContainer());
    }

    @NotNull
    @Override
    protected Component commandInfo(@NotNull CMDExecutor command, @NotNull CommandCause cause) {
        return text().append(command.getUsage(cause), text(" - ", DARK_GRAY), command.getDescription(cause)).build();
    }

    @Override
    public CommandResult execute(@NotNull CommandContext context) {
        return super.execute(context);
    }

    @Override
    public @NotNull Component getUsage(CommandCause commandCause) {
        return text("/ss");
    }

    @Override
    public List<String> getAliases() {
        return List.of("ss");
    }

    @Override
    public @NotNull List<CMDExecutor> getChildren() {
        return List.of(new SSClaim(), new SSDelete(), new SSEdit(), new SSMyBuilds(), new SSNew(), new SSReload(), new SSReward(), new SSSubmit(), new SSView(), new SSViewAll());
    }

    @Override
    public @NotNull String getName() {
        return "serversaturday";
    }
}
