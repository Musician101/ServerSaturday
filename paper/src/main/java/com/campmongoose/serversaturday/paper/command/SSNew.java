package com.campmongoose.serversaturday.paper.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.paper.submission.PaperSubmissionsUtil;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class SSNew extends ServerSaturdayCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new SSName());
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return canUseSubmit(sender);
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Create a new build to be submitted.";
    }

    @NotNull
    @Override
    public String name() {
        return "new";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/ss new <name>";
    }

    static class SSName extends ServerSaturdayCommand implements ArgumentCommand<String> {

        @Override
        public int execute(@NotNull CommandContext<CommandSender> context) {
            Player player = (Player) context.getSource();
            getSubmitter(player).newBuild(StringArgumentType.getString(context, name()), PaperSubmissionsUtil.asSSLocation(player.getLocation()));
            player.sendMessage(text(Messages.PREFIX + "New build created successfully.", GREEN));
            return 1;
        }

        @NotNull
        @Override
        public String name() {
            return "name";
        }

        @NotNull
        @Override
        public ArgumentType<String> type() {
            return StringArgumentType.greedyString();
        }
    }
}
