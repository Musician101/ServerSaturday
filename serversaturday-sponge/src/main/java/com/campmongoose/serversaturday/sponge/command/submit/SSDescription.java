package com.campmongoose.serversaturday.sponge.command.submit;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.command.SSCommandException;
import com.campmongoose.serversaturday.sponge.command.AbstractSpongeCommand;
import com.campmongoose.serversaturday.sponge.command.args.BuildCommandElement;
import com.campmongoose.serversaturday.sponge.gui.book.BookGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.build.EditBuildGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
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

public class SSDescription extends AbstractSpongeCommand {

    //TODO left off here
    //TODO go through all commands to use the new custom command elements
    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        return arguments.<SpongeBuild>getOne(BuildCommandElement.KEY).map(build -> {
            if (source instanceof Player) {
                Player player = (Player) source;
                ItemStack itemStack = player.getItemInHand(HandTypes.MAIN_HAND).orElse(ItemStack.empty());
                if (itemStack.getItem() == ItemTypes.NONE) {
                    player.sendMessage(Text.builder(Messages.HAND_NOT_EMPTY).color(TextColors.RED).build());
                    return CommandResult.empty();
                }

                if (BookGUI.isEditing(player)) {
                    player.sendMessage(Text.builder(Messages.EDIT_IN_PROGRESS).color(TextColors.RED).build());
                    return CommandResult.empty();
                }

                SpongeSubmitter submitter;
                try {
                    submitter = getSubmitter(player);
                }
                catch (SSCommandException e) {
                    player.sendMessage(Text.builder(Messages.ERROR).color(TextColors.RED).build());
                    return CommandResult.empty();
                }

                new BookGUI(player, build, build.getDescription(), pages -> {
                    build.setDescription(pages);
                    new EditBuildGUI(build, submitter, player, null);
                });
                return CommandResult.success();
            }

            return playerOnly(source);
        }).orElse(CommandResult.empty());
    }
}
