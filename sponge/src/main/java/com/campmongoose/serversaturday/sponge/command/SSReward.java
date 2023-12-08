package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.command.parameter.managed.ValueParameter.Simple;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.GameProfileManager;

import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class SSReward extends ServerSaturdayCommand {

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        return context.one(value).map(gameProfile -> {
            getRewardHandler().giveReward(gameProfile.uuid());
            context.sendMessage(Messages.rewardsGiven(gameProfile.name().orElse("null")));
            Sponge.server().player(gameProfile.uuid()).ifPresent(player -> player.sendMessage(Messages.REWARDS_WAITING));
            return CommandResult.success();
        }).orElseThrow(() -> new CommandException(Component.text(Messages.PLAYER_NOT_FOUND, RED)));
    }

    @NotNull
    @Override
    public String usage() {
        return "/ss reward <player>";
    }

    @NotNull
    @Override
    public String description() {
        return "Give a player a reward.";
    }

    @NotNull
    @Override
    public String name() {
        return "reward";
    }

    @Override
    public boolean canUse(@NotNull CommandContext context) {
        return context.hasPermission(Permissions.FEATURE);
    }

    private final Value<GameProfile> value = Parameter.builder(Parameter.key("player", GameProfile.class)).addParser(new Simple<>() {

        private GameProfileManager manager() {
            return Sponge.server().gameProfileManager();
        }

        @Override
        public Optional<? extends GameProfile> parseValue(CommandCause commandCause, Mutable reader) throws ArgumentParseException {
            try {
                return Optional.of(manager().basicProfile(reader.parseString()).get());
            }
            catch (InterruptedException | ExecutionException e) {
                return Optional.empty();
            }
        }

        @Override
        public List<CommandCompletion> complete(CommandCause context, String currentInput) {
            return manager().cache().stream().map(gp -> gp.name().orElse("null")).filter(s -> s.startsWith(currentInput)).map(CommandCompletion::of).toList();
        }
    }).build();

    @NotNull
    @Override
    public Command.Parameterized toCommand() {
        return Command.builder().shortDescription(Component.text(description())).permission(Permissions.ADMIN).executor(this).addParameter(value).build();
    }
}
