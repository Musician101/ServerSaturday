package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Messages;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class SSNew extends ServerSaturdayCommand implements LiteralCommand {

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new SSName());
    }

    @Override
    public boolean canUse(@Nonnull CommandSender sender) {
        return canUseSubmit(sender);
    }

    @Nonnull
    @Override
    public String description() {
        return "Create a new build to be submitted.";
    }

    @Nonnull
    @Override
    public String name() {
        return "new";
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/ss new <name>";
    }

    static class SSName extends ServerSaturdayCommand implements ArgumentCommand<String> {

        @Override
        public int execute(@Nonnull CommandContext<CommandSender> context) {
            Player player = (Player) context.getSource();
            getSubmitter(player).newBuild(StringArgumentType.getString(context, name()), player.getLocation());
            player.sendMessage(text(Messages.PREFIX + "New build created successfully.", GREEN));
            return 1;
        }

        @Nonnull
        @Override
        public String name() {
            return "name";
        }

        @Nonnull
        @Override
        public ArgumentType<String> type() {
            return StringArgumentType.greedyString();
        }
    }
}
