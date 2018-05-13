package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.gui.chest.ChestGUIs;
import com.campmongoose.serversaturday.sponge.command.args.SubmitterBuildCommandElement;
import com.campmongoose.serversaturday.sponge.gui.chest.SpongeChestGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.SpongeChestGUIBuilder;
import com.campmongoose.serversaturday.sponge.gui.chest.SpongeChestGUIs;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.Map.Entry;
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

public class SSFeature extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        if (source instanceof Player) {
            Player player = (Player) source;
            ChestGUIs<SpongeChestGUIBuilder, Class<? extends ClickInventoryEvent>, SpongeChestGUI, Inventory, SpongeBuild, Location<World>, Player, ItemStack, Text, SpongeSubmitter> chestGUIs = SpongeChestGUIs.INSTANCE;
            return arguments.<SpongeSubmitter>getOne(SubmitterBuildCommandElement.KEY).map(submitter -> {
                chestGUIs.submitter(1, player, submitter, null);
                return CommandResult.success();
            }).orElseGet(() -> arguments.<Entry<SpongeSubmitter, SpongeBuild>>getOne(SubmitterBuildCommandElement.KEY).map(entry -> {
                SpongeSubmitter submitter = entry.getKey();
                SpongeBuild build = entry.getValue();
                build.setFeatured(!build.featured());
                chestGUIs.viewBuild(build, submitter, player, null);
                return CommandResult.success();
            }).orElseGet(() -> {
                chestGUIs.allSubmissions(1, player, null);
                return CommandResult.success();
            }));
        }

        return playerOnly(source);
    }
}
