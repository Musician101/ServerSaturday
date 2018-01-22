package com.campmongoose.serversaturday.sponge.command.submit;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.command.SSCommandExecutor;
import com.campmongoose.serversaturday.sponge.command.args.BuildCommandElement;
import com.campmongoose.serversaturday.sponge.gui.SpongeBookGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.SpongeChestGUIs;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

public class SSDescription extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        return arguments.<SpongeBuild>getOne(BuildCommandElement.KEY).map(build -> {
            if (source instanceof Player) {
                Player player = (Player) source;
                ItemStack itemStack = player.getItemInHand(HandTypes.MAIN_HAND).orElse(ItemStack.empty());
                if (itemStack.getType() == ItemTypes.NONE) {
                    player.sendMessage(Text.of(TextColors.RED, Messages.HAND_NOT_EMPTY));
                    return CommandResult.empty();
                }

                if (SpongeBookGUI.isEditing(player)) {
                    player.sendMessage(Text.of(TextColors.RED, Messages.EDIT_IN_PROGRESS));
                    return CommandResult.empty();
                }

                return getSubmitter(player).map(submitter -> {
                    new SpongeBookGUI(player, build, build.getDescription().stream().map(Text::of).collect(Collectors.toList()), pages -> {
                        build.setDescription(pages.stream().map(Text::toPlain).collect(Collectors.toList()));
                        SpongeChestGUIs.INSTANCE.editBuild(build, submitter, player, null);
                    });
                    return CommandResult.success();
                }).orElse(CommandResult.empty());
            }

            return playerOnly(source);
        }).orElse(CommandResult.empty());
    }
}
