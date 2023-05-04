package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.ServerSaturday;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.musician101.bukkitier.Bukkitier;
import io.papermc.paper.plugin.configuration.PluginMeta;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.command.CommandSender;

import static io.musician101.bukkitier.Bukkitier.literal;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class SSCommand {

    private static final List<ServerSaturdayCommand> COMMANDS = List.of(new SSClaim(), new SSEdit(), new SSReload(), new SSReward(), new SSSubmit(), new SSView(), new SSViewAll());

    private SSCommand() {

    }

    private static ServerSaturday getPlugin() {
        return ServerSaturday.getInstance();
    }

    static Component joinText(ComponentLike... components) {
        return Component.join(JoinConfiguration.noSeparators(), components);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void registerCommand() {
        LiteralArgumentBuilder<CommandSender> builder = literal("serversaturday").executes(context -> {
            CommandSender sender = context.getSource();
            PluginMeta meta = ServerSaturday.getInstance().getPluginMeta();
            sender.sendMessage(joinText(text("===== ", GREEN), text("ServerSaturday v" + meta.getVersion()), text(" by ", GREEN), text("Musician101"), text(" =====", GREEN)));
            String baseCMD = "/ss ";
            sender.sendMessage(joinText(text(baseCMD), text("Displays help and plugin info.", AQUA)));
            COMMANDS.stream().filter(cmd -> sender.hasPermission("ss." + cmd.getPermission())).forEach(cmd -> sender.sendMessage(joinText(text(baseCMD + cmd.getName() + " " + cmd.getUsage() + " "), text(cmd.getDescription(), AQUA))));
            return 1;
        });
        COMMANDS.forEach(command -> builder.then(command.toBukkitier()));
        Bukkitier.registerCommand(getPlugin(), builder);
    }
}
