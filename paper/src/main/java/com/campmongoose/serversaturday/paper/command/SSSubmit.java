package com.campmongoose.serversaturday.paper.command;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Build;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import java.util.List;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class SSSubmit extends ServerSaturdayCommand implements LiteralCommand {

    @NotNull
    @Override
    public String getName() {
        return "submit";
    }

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
        return "Submit your build to be featured.";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/ss submit <build>";
    }

    static class SSBuild extends com.campmongoose.serversaturday.paper.command.SSBuild {

        @Override
        public int execute(@NotNull CommandContext<CommandSender> context) {
            Player player = (Player) context.getSource();
            Build build = (Build) context.getArgument(Commands.BUILD, Map.class).get(player.getUniqueId());
            if (build == null) {
                player.sendMessage(Messages.BUILD_DOES_NOT_EXIST);
                return 0;
            }

            build.setSubmitted(!build.submitted());
            player.sendMessage(text(Messages.PREFIX + "Build " + (build.submitted() ? "has been submitted." : " is no longer submitted."), GREEN));
            return 1;
        }
    }
}
