package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Messages;
import com.campmongoose.serversaturday.command.argument.BuildArgumentType.Holder;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class SSDelete extends ServerSaturdayCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new SSBuild());
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return canUseSubmit(sender);
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Delete a submission.";
    }

    @NotNull
    @Override
    public String name() {
        return "delete";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/ss delete <build>";
    }

    static class SSBuild extends com.campmongoose.serversaturday.command.SSBuild {

        @Override
        public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
            Player player = (Player) context.getSource();
            Submitter submitter = getSubmitter(player);
            Optional<Build> build = context.getArgument(name(), Holder.class).get(submitter);
            if (build.isEmpty()) {
                player.sendMessage(Messages.BUILD_DOES_NOT_EXIST);
                return 0;
            }

            submitter.getBuilds().remove(build.get());
            player.sendMessage(text(Messages.PREFIX + "Build deleted.", GREEN));
            return 1;
        }
    }
}
