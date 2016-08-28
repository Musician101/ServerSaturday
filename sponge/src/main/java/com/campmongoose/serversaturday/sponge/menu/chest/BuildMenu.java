package com.campmongoose.serversaturday.sponge.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.command.sscommand.SSFeature;
import com.campmongoose.serversaturday.sponge.command.sscommand.submit.SSDescription;
import com.campmongoose.serversaturday.sponge.command.sscommand.submit.SSLocation;
import com.campmongoose.serversaturday.sponge.command.sscommand.submit.SSRemove;
import com.campmongoose.serversaturday.sponge.command.sscommand.submit.SSRename;
import com.campmongoose.serversaturday.sponge.command.sscommand.submit.SSResourcePack;
import com.campmongoose.serversaturday.sponge.command.sscommand.submit.SSSubmit;
import com.campmongoose.serversaturday.sponge.command.sscommand.view.SSGoto;
import com.campmongoose.serversaturday.sponge.command.sscommand.view.SSViewDescription;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.GoldenApples;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.OrderedInventory;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.UUID;

public class BuildMenu extends ChestMenu
{
    public BuildMenu(SpongeBuild build, SpongeSubmitter submitter, OrderedInventory inv, UUID viewer)//NOSONAR
    {
        super(inv, event ->//NOSONAR
        {
            Player player = event.getPlayer();
            @SuppressWarnings("OptionalGetWithoutIsPresent")
            String name = event.getItem().get(Keys.DISPLAY_NAME).get().toPlain();
            switch (name)
            {
                case MenuText.BACK:
                    submitter.openMenu(1, player.getUniqueId());
                    break;
                case MenuText.CHANGE_DESCRIPTION_NAME:
                    new SSDescription().process(player, build.getName());
                    break;
                case MenuText.CHANGE_LOCATION_NAME:
                    new SSLocation().process(player, build.getName());
                    break;
                case MenuText.CHANGE_RESOURCE_PACK_NAME:
                    new SSResourcePack().process(player, build.getName());
                    break;
                case MenuText.DELETE_NAME:
                    new SSRemove().process(player, build.getName());
                    break;
                case MenuText.DESCRIPTION_NAME:
                    new SSViewDescription().process(player, submitter.getName() + " " + build.getName());
                    break;
                case MenuText.FEATURE_NAME:
                    new SSFeature().process(player, submitter.getName() + " " + build.getName());
                    return;
                case MenuText.RENAME_NAME:
                    new SSRename().process(player, build.getName());
                    break;
                case MenuText.SUBMIT_UNREADY_NAME:
                    new SSSubmit().process(player, build.getName());
                    break;
                case MenuText.TELEPORT_NAME:
                    new SSGoto().process(player, submitter.getName() + " " + build.getName());
                    break;
                default:
            }
        });

        Location<World> location = build.getLocation();
        if (viewer.equals(submitter.getUUID()))
        {
            setOption(0, ItemStack.of(ItemTypes.PAPER, 1), MenuText.RENAME_NAME, MenuText.RENAME_DESC);
            setOption(1, ItemStack.of(ItemTypes.COMPASS, 1), MenuText.CHANGE_LOCATION_NAME, MenuText.CHANGE_LOCATION_DESC.toArray(new String[3]));
            setOption(2, ItemStack.of(ItemTypes.BOOK, 1), MenuText.CHANGE_DESCRIPTION_NAME, MenuText.CHANGE_DESCRIPTION_DESC);
            setOption(3, ItemStack.of(ItemTypes.PAINTING, 1), MenuText.CHANGE_RESOURCE_PACK_NAME, MenuText.CHANGE_RESOURCE_PACK_DESC.toArray(new String[2]));
            setOption(4, ItemStack.of(ItemTypes.FLINT_AND_STEEL, 1), MenuText.SUBMIT_UNREADY_NAME, build.submitted(), MenuText.SUBMIT_UNREADY_DESC.toArray(new String[2]));
            setOption(5, ItemStack.of(ItemTypes.COMPASS, 1), MenuText.TELEPORT_NAME, MenuText.teleportDesc(location.getExtent().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            setOption(6, ItemStack.of(ItemTypes.BARRIER, 1), MenuText.DELETE_NAME, MenuText.DELETE_DESC.toArray(new String[2]));
            Optional<Player> player = Sponge.getServer().getPlayer(viewer);
            if (!player.isPresent())
                return;

            //noinspection OptionalGetWithoutIsPresent
            if (player.get().hasPermission(Permissions.FEATURE))
            {
                if (build.featured())
                    setOption(7, ItemStack.builder().itemType(ItemTypes.GOLDEN_APPLE).add(Keys.GOLDEN_APPLE_TYPE, GoldenApples.ENCHANTED_GOLDEN_APPLE).build(), MenuText.FEATURE_NAME, MenuText.FEATURE_DESC.toArray(new String[2]));
                else
                    setOption(7, ItemStack.of(ItemTypes.GOLDEN_APPLE, 1), MenuText.FEATURE_NAME, MenuText.FEATURE_DESC.toArray(new String[2]));
            }
            else
                setOption(7, ItemStack.of(ItemTypes.STAINED_GLASS_PANE, 1));

            setOption(8, ItemStack.of(ItemTypes.ARROW, 1), MenuText.BACK);
        }
        else
        {
            setOption(0, ItemStack.of(ItemTypes.COMPASS, 1), MenuText.TELEPORT_NAME, MenuText.teleportDesc(location.getExtent().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            setOption(1, ItemStack.of(ItemTypes.BOOK, 1), MenuText.CHANGE_DESCRIPTION_NAME, MenuText.CHANGE_DESCRIPTION_DESC);
            setOption(2, ItemStack.of(ItemTypes.PAINTING, 1), MenuText.RESOURCE_PACK_NAME, build.getResourcePack());
            Optional<Player> player = Sponge.getServer().getPlayer(viewer);
            if (!player.isPresent())
                return;

            //noinspection OptionalGetWithoutIsPresent
            if (player.get().hasPermission(Permissions.FEATURE))
            {
                if (build.featured())
                    setOption(3, ItemStack.builder().itemType(ItemTypes.GOLDEN_APPLE).add(Keys.GOLDEN_APPLE_TYPE, GoldenApples.ENCHANTED_GOLDEN_APPLE).build(), MenuText.FEATURE_NAME, MenuText.FEATURE_DESC.toArray(new String[2]));
                else
                    setOption(3, ItemStack.of(ItemTypes.GOLDEN_APPLE, 1), MenuText.FEATURE_NAME, MenuText.FEATURE_DESC.toArray(new String[2]));
            }
            else
                setOption(3, ItemStack.of(ItemTypes.STAINED_GLASS_PANE, 1));

            setOption(4, ItemStack.of(ItemTypes.STAINED_GLASS_PANE, 1));
            setOption(5, ItemStack.of(ItemTypes.STAINED_GLASS_PANE, 1));
            setOption(6, ItemStack.of(ItemTypes.STAINED_GLASS_PANE, 1));
            setOption(7, ItemStack.of(ItemTypes.STAINED_GLASS_PANE, 1));
            setOption(8, ItemStack.of(ItemTypes.ARROW, 1), MenuText.BACK);
        }
    }
}
