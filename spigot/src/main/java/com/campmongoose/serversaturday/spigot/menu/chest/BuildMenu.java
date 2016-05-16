package com.campmongoose.serversaturday.spigot.menu.chest;

import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSDescription;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSLocation;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSRename;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSSubmit;
import com.campmongoose.serversaturday.spigot.command.sscommand.view.SSGoto;
import com.campmongoose.serversaturday.spigot.command.sscommand.view.SSViewDescription;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import com.campmongoose.serversaturday.spigot.command.sscommand.SSFeature;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSRemove;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSResourcePack;
import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BuildMenu extends ChestMenu
{
    public BuildMenu(SpigotBuild build, SpigotSubmitter submitter, Inventory inv, UUID viewer)//NOSONAR
    {
        super(inv, event ->//NOSONAR
        {
            //TODO google doc export
            Player player = event.getPlayer();
            String name = event.getItem().getItemMeta().getDisplayName();
            switch (name)
            {
                case MenuText.BACK:
                    submitter.openMenu(1, player.getUniqueId());
                    break;
                case MenuText.CHANGE_DESCRIPTION_NAME:
                    new SSDescription().onCommand(player, build.getName());
                    break;
                case MenuText.CHANGE_LOCATION_NAME:
                    new SSLocation().onCommand(player, build.getName());
                    break;
                case MenuText.CHANGE_RESOURCE_PACK_NAME:
                    new SSResourcePack().onCommand(player, build.getName());
                    break;
                case MenuText.DELETE_NAME:
                    new SSRemove().onCommand(player, build.getName());
                    break;
                case MenuText.DESCRIPTION_NAME:
                    new SSViewDescription().onCommand(player, submitter.getName(), build.getName());
                    break;
                case MenuText.FEATURE_NAME:
                    new SSFeature().onCommand(player, submitter.getName(), build.getName());
                    return;
                case MenuText.RENAME_NAME:
                    new SSRename().onCommand(player, build.getName());
                    break;
                case MenuText.SUBMIT_UNREADY_NAME:
                    new SSSubmit().onCommand(player, build.getName());
                    break;
                case MenuText.TELEPORT_NAME:
                    new SSGoto().onCommand(player, submitter.getName(), build.getName());
                    break;
                default:
            }
        });

        Location location = build.getLocation();
        if (viewer.equals(submitter.getUUID()))
        {
            setOption(0, new ItemStack(Material.PAPER), MenuText.RENAME_NAME, MenuText.RENAME_DESC);
            setOption(1, new ItemStack(Material.COMPASS), MenuText.CHANGE_LOCATION_NAME, MenuText.CHANGE_LOCATION_DESC.toArray(new String[3]));
            setOption(2, new ItemStack(Material.BOOK), MenuText.CHANGE_DESCRIPTION_NAME, MenuText.CHANGE_DESCRIPTION_DESC);
            setOption(3, new ItemStack(Material.PAINTING), MenuText.CHANGE_RESOURCE_PACK_NAME, MenuText.CHANGE_RESOURCE_PACK_DESC.toArray(new String[2]));
            setOption(4, new ItemStack(Material.FLINT_AND_STEEL), MenuText.SUBMIT_UNREADY_NAME, build.submitted(), MenuText.SUBMIT_UNREADY_DESC.toArray(new String[2]));
            setOption(5, new ItemStack(Material.COMPASS), MenuText.TELEPORT_NAME, MenuText.teleportDesc(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            setOption(6, new ItemStack(Material.BARRIER), MenuText.DELETE_NAME, MenuText.DELETE_DESC.toArray(new String[2]));
            if (Bukkit.getPlayer(viewer).hasPermission(Permissions.FEATURE))
            {
                if (build.featured())
                    setOption(7, new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1), MenuText.FEATURE_NAME, MenuText.FEATURE_DESC.toArray(new String[2]));
                else
                    setOption(7, new ItemStack(Material.GOLDEN_APPLE), MenuText.FEATURE_NAME, MenuText.FEATURE_DESC.toArray(new String[2]));
            }
            else
                setOption(7, new ItemStack(Material.STAINED_GLASS_PANE));

            setOption(8, new ItemStack(Material.ARROW), MenuText.BACK);
        }
        else
        {
            setOption(0, new ItemStack(Material.COMPASS), MenuText.TELEPORT_NAME, MenuText.teleportDesc(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            setOption(1, new ItemStack(Material.BOOK), MenuText.CHANGE_DESCRIPTION_NAME, MenuText.CHANGE_DESCRIPTION_DESC);
            setOption(2, new ItemStack(Material.PAINTING), MenuText.RESOURCE_PACK_NAME, build.getResourcePack());
            if (Bukkit.getPlayer(viewer).hasPermission(Permissions.FEATURE))
            {
                if (build.featured())
                    setOption(3, new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1), MenuText.FEATURE_NAME, MenuText.FEATURE_DESC.toArray(new String[2]));
                else
                    setOption(3, new ItemStack(Material.GOLDEN_APPLE), MenuText.FEATURE_NAME, MenuText.FEATURE_DESC.toArray(new String[2]));
            }
            else
                setOption(3, new ItemStack(Material.STAINED_GLASS_PANE));

            setOption(4, new ItemStack(Material.STAINED_GLASS_PANE));
            setOption(5, new ItemStack(Material.STAINED_GLASS_PANE));
            setOption(6, new ItemStack(Material.STAINED_GLASS_PANE));
            setOption(7, new ItemStack(Material.STAINED_GLASS_PANE));
            setOption(8, new ItemStack(Material.ARROW), MenuText.BACK);
        }
    }
}
