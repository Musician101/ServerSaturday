package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.Reference.Permissions;
import com.campmongoose.serversaturday.command.argument.BuildArgumentType;
import com.campmongoose.serversaturday.submission.Build;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Map;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.musician101.bukkitier.Bukkitier.argument;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class SSSubmit extends ServerSaturdayCommand {

    @Override
    protected void addToBuilder(LiteralArgumentBuilder<CommandSender> builder) {
        builder.then(argument(Commands.BUILD, new BuildArgumentType()).executes(context -> {
            Player player = (Player) context.getSource();
            Build build = (Build) context.getArgument(Commands.BUILD, Map.class).get(player.getUniqueId());
            if (build == null) {
                player.sendMessage(Messages.BUILD_DOES_NOT_EXIST);
                return 0;
            }
            build.setSubmitted(!build.submitted());
            player.sendMessage(text(Messages.PREFIX + "Build " + (build.submitted() ? "has been submitted." : " is no longer submitted."), GREEN));
            return 1;
        }));
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Submit your build to be featured.";
    }

    @Nonnull
    @Override
    public String getName() {
        return "submit";
    }

    @Nonnull
    @Override
    public String getPermission() {
        return Permissions.SUBMIT;
    }

    @Nonnull
    @Override
    public String getUsage() {
        return "<build>";
    }

    @Override
    protected boolean isPlayerOnly() {
        return true;
    }
}
