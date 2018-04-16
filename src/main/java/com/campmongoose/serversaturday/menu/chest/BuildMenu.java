package com.campmongoose.serversaturday.menu.chest;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.command.sscommand.SSFeature;
import com.campmongoose.serversaturday.command.sscommand.submit.SSDescription;
import com.campmongoose.serversaturday.command.sscommand.submit.SSLocation;
import com.campmongoose.serversaturday.command.sscommand.submit.SSRemove;
import com.campmongoose.serversaturday.command.sscommand.submit.SSRename;
import com.campmongoose.serversaturday.command.sscommand.submit.SSResourcePack;
import com.campmongoose.serversaturday.command.sscommand.submit.SSSubmit;
import com.campmongoose.serversaturday.command.sscommand.view.SSGoto;
import com.campmongoose.serversaturday.command.sscommand.view.SSViewDescription;
import com.campmongoose.serversaturday.command.sscommand.view.SSViewResourcePack;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BuildMenu extends ChestMenu {

    public BuildMenu(Build build, Submitter submitter, Inventory inv, UUID viewer) {
        super(inv, event -> Bukkit.getScheduler().scheduleSyncDelayedTask(ServerSaturday.instance(), () -> {
            int slot = event.getSlot();
            Player player = event.getPlayer();
            String name = event.getItem().getItemMeta().getDisplayName();
            if (slot == 0) {
                if (name.equalsIgnoreCase("Rename")) {
                    new SSRename().onCommand(player, build.getName());
                }
                else if (name.equalsIgnoreCase("Teleport")) {
                    new SSGoto().onCommand(player, submitter.getName(), build.getName());
                }
            }
            else if (slot == 1) {
                if (name.equals("Change Location")) {
                    new SSLocation().onCommand(player, build.getName());
                }
                else if (name.equals("Description")) {
                    new SSViewDescription().onCommand(player, submitter.getName(), build.getName());
                }
            }
            else if (slot == 2) {
                if (name.equals("Change Description")) {
                    new SSDescription().onCommand(player, build.getName());
                }
                else if (name.equals("Resource Pack")) {
                    new SSViewResourcePack().onCommand(player, submitter.getName(), build.getName());
                }
            }
            else if (slot == 3) {
                if (name.equals("Change Resource Pack")) {
                    new SSResourcePack().onCommand(player, build.getName());
                }
                else if (name.equals("Feature")) {
                    new SSFeature().onCommand(player, submitter.getName(), build.getName());
                }
            }
            else if (slot == 4) {
                if (name.equals("Submit/Unready")) {
                    new SSSubmit().onCommand(player, build.getName());
                }
            }
            else if (slot == 5) {
                if (name.equals("Teleport")) {
                    new SSGoto().onCommand(player, submitter.getName(), build.getName());
                }
            }
            else if (slot == 6) {
                if (name.equals("Delete")) {
                    new SSRemove().onCommand(player, build.getName());
                }
            }
            else if (slot == 7) {
                if (name.equals("Feature")) {
                    new SSFeature().onCommand(player, submitter.getName(), build.getName());
                }
            }
            else if (slot == 8) {
                submitter.openMenu(1, Bukkit.getPlayer(viewer));
            }

            if (name.equals(" ")) {
                event.setWillClose(false);
                event.setWillDestroy(false);
            }
        }));

        Location location = build.getLocation();
        if (viewer.equals(submitter.getUUID())) {
            setOption(0, new ItemStack(Material.PAPER), "Rename", "Rename this build.");
            setOption(1, new ItemStack(Material.COMPASS), "Change Location", "Change the warp location for this build", "to where you are currently standing.", "WARNING: This will affect which direction", "people face when they teleport to your build.");
            setOption(2, new ItemStack(Material.BOOK), "Change Description", "Add or change the description to this build.");
            setOption(3, new ItemStack(Material.PAINTING), "Change Resource Pack", "Change the recommended resource", "pack for this build.");
            setOption(4, new ItemStack(Material.FLINT_AND_STEEL), "Submit/Unready", build.submitted(), "Add or remove your build from", "the list of ready builds.");
            setOption(5, new ItemStack(Material.COMPASS), "Teleport", "Click to teleport.", "- World: " + location.getWorld().getName(), "- X: " + location.getBlockX(), "- Y: " + location.getBlockY(), "- Z: " + location.getBlockZ());
            setOption(6, new ItemStack(Material.BARRIER), "Delete", "THIS WILL DELETE THIS BUILD", "FROM THE SUBMISSION LIST!!!");
            if (Bukkit.getPlayer(viewer).hasPermission("ss.feature")) {
                if (build.featured()) {
                    setOption(7, new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1), "Feature", "Set whether this build has been covered in", "an episode of Server Saturday.");
                }
                else {
                    setOption(7, new ItemStack(Material.GOLDEN_APPLE), "Feature", "Set whether this build has been covered in", "an episode of Server Saturday.");
                }
            }
            else {
                setOption(7, new ItemStack(Material.STAINED_GLASS_PANE));
            }

            setOption(8, new ItemStack(Material.ARROW), "Back");
        }
        else {
            setOption(0, new ItemStack(Material.COMPASS), "Teleport", "Click to teleport.", "- World: " + location.getWorld().getName(), "- X: " + location.getBlockX(), "- Y: " + location.getBlockY(), "- Z: " + location.getBlockZ());
            setOption(1, new ItemStack(Material.BOOK), "Description", "Click to get a book with this build's description.");
            setOption(2, new ItemStack(Material.PAINTING), "Resource Pack", "Click to get a book that lists this", "build's recommended resource packs.");
            if (Bukkit.getPlayer(viewer).hasPermission("ss.feature")) {
                if (build.featured()) {
                    setOption(3, new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1), "Feature", build.featured(), "Set whether this build has been covered in", "an episode of Server Saturday.");
                }
                else {
                    setOption(3, new ItemStack(Material.GOLDEN_APPLE), "Feature", build.featured(), "Set whether this build has been covered in", "an episode of Server Saturday.");
                }
            }
            else {
                setOption(3, new ItemStack(Material.STAINED_GLASS_PANE));
            }

            setOption(4, new ItemStack(Material.STAINED_GLASS_PANE));
            setOption(5, new ItemStack(Material.STAINED_GLASS_PANE));
            setOption(6, new ItemStack(Material.STAINED_GLASS_PANE));
            setOption(7, new ItemStack(Material.STAINED_GLASS_PANE));
            setOption(8, new ItemStack(Material.ARROW), "Back");
        }
    }
}
