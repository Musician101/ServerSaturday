package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.metadata.PluginMetadata;
import org.spongepowered.plugin.metadata.model.PluginContributor;

import static com.campmongoose.serversaturday.sponge.SpongeServerSaturday.getPlugin;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class SSCommand {

    private SSCommand() {

    }

    private static TextComponent header() {
        PluginMetadata pmd = getPlugin().getPluginContainer().metadata();
        TextComponent begin = Component.text("> ===== ", DARK_GREEN);
        TextComponent middle = Component.text(pmd.name().orElse(pmd.id()), GREEN);
        TextComponent end = Component.text(" ===== <", DARK_GREEN);
        begin = begin.children(List.of(middle, end));
        TextComponent developed = Component.text("Developed by ", GOLD);
        List<String> authors = pmd.contributors().stream().map(PluginContributor::name).toList();
        int last = authors.size() - 1;
        TextComponent authorsComponent = Component.text(switch (last){
            case 0 -> authors.get(0);
            case 1 -> String.join(" and ", authors);
            default -> String.join(", and ", String.join(", ", authors.subList(0, last)), authors.get(last));
        }, TextColor.color(0xBDB76B));
        return begin.hoverEvent(HoverEvent.showText(Component.textOfChildren(developed, authorsComponent)));
    }

    private static TextComponent commandInfo(@NotNull ServerSaturdayCommand command) {
        TextComponent cmd = Component.text(command.usage());
        TextComponent dash = Component.text(" - ", DARK_GRAY);
        TextComponent description = Component.text(command.description(), GRAY);
        return cmd.children(List.of(dash, description));
    }

    public static void registerCommand(RegisterCommandEvent<Command> event) {
        PluginContainer pluginContainer = SpongeServerSaturday.getPlugin().getPluginContainer();
        Command.Builder builder = Command.builder().executor((context) -> {
            CommandCause cause = context.cause();
            cause.sendMessage(header());
            arguments().stream().filter(c -> c.canUse(context)).forEach(c -> cause.sendMessage(commandInfo(c)));
            return CommandResult.success();
        });
        arguments().forEach(c -> builder.addChild(c.toCommand()));
        event.register(pluginContainer, builder.build(), "serversaturday", "ss");
    }

    private static List<ServerSaturdayCommand> arguments() {
        return List.of(new SSClaim(), new SSDelete(), new SSEdit(), new SSMyBuilds(), new SSNew(), new SSReload(), new SSReward(), new SSSubmit(), new SSView(), new SSViewAll());
    }
}
