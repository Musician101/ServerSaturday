package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.command.parameter.managed.ValueParameter.Simple;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.GameProfileManager;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

public class SSReward extends ServerSaturdayCommand {

    private final Value<GameProfile> value = Parameter.builder(Parameter.key("player", GameProfile.class)).addParser(new Simple<>() {

        @Override
        public List<CommandCompletion> complete(CommandCause context, String currentInput) {
            return manager().cache().stream().map(gp -> gp.name().orElse("null")).filter(s -> s.startsWith(currentInput)).map(CommandCompletion::of).toList();
        }

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
    }).build();

    @Override
    public CommandResult execute(CommandContext context) {
        GameProfile profile = context.requireOne(value);
        getRewardHandler().giveReward(profile.uuid());
        context.sendMessage(Messages.rewardsGiven(profile.name().orElse("null")));
        Sponge.server().player(profile.uuid()).ifPresent(player -> player.sendMessage(Messages.REWARDS_WAITING));
        return CommandResult.success();
    }

    @NotNull
    @Override
    public Component getDescription(@NotNull CommandCause cause) {
        return text("Give a player a reward.", GRAY);
    }

    @NotNull
    @Override
    public String getName() {
        return "reward";
    }

    @Override
    public @NotNull Optional<String> getPermission() {
        return Optional.of(Permissions.FEATURE);
    }

    @NotNull
    @Override
    public Component getUsage(@NotNull CommandCause cause) {
        return text("/ss reward <player>");
    }
}
