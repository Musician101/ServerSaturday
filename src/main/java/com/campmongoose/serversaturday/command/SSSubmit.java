package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.submission.Build;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class SSSubmit extends ServerSaturdayCommand implements LiteralCommand {

    @Nonnull
    @Override
    public String name() {
        return "submit";
    }

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new SSBuild());
    }

    @Override
    public boolean canUse(@Nonnull CommandSender sender) {
        return canUseSubmit(sender);
    }

    @Nonnull
    @Override
    public String description() {
        return "Submit your build to be featured.";
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/ss submit <build>";
    }

    static class SSBuild extends com.campmongoose.serversaturday.command.SSBuild {

        @Override
        public int execute(@Nonnull CommandContext<CommandSender> context) {
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
