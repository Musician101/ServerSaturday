package com.campmongoose.serversaturday.sponge.command.submit;

import com.campmongoose.serversaturday.common.gui.chest.ChestGUIs;
import com.campmongoose.serversaturday.sponge.command.SSCommandExecutor;
import com.campmongoose.serversaturday.sponge.command.args.BuildCommandElement;
import com.campmongoose.serversaturday.sponge.gui.chest.SpongeChestGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.SpongeChestGUIBuilder;
import com.campmongoose.serversaturday.sponge.gui.chest.SpongeChestGUIs;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SSEdit extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        if (source instanceof Player) {
            Player player = (Player) source;
            ChestGUIs<SpongeChestGUIBuilder, Class<? extends ClickInventoryEvent>, SpongeChestGUI, Inventory, SpongeBuild, Location<World>, Player, ItemStack, Text, SpongeSubmitter> chestGUIs = SpongeChestGUIs.INSTANCE;
            return getSubmitter(player).map(submitter -> arguments.<SpongeBuild>getOne(BuildCommandElement.KEY).map(build -> {
                chestGUIs.editBuild(build, submitter, player, null);
                return CommandResult.success();
            }).orElseGet(() -> {
                chestGUIs.submitter(1, player, submitter, null);
                return CommandResult.success();
            })).orElse(CommandResult.empty());
        }

        return playerOnly(source);
    }
}
