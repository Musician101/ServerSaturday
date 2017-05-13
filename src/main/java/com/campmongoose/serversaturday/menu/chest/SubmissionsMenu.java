package com.campmongoose.serversaturday.menu.chest;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Submissions;
import com.campmongoose.serversaturday.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.submission.Submitter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SubmissionsMenu extends ChestMenu {

    public SubmissionsMenu(int page, Inventory inv, UUID viewer) {
        super(inv, event -> Bukkit.getScheduler().scheduleSyncDelayedTask(ServerSaturday.instance(), () -> {
            ItemStack itemStack = event.getItem();
            int slot = event.getSlot();
            Player player = event.getPlayer();
            String name = itemStack.getItemMeta().getDisplayName();
            try {
                Submissions submissions = ServerSaturday.instance().getSubmissions();
                if (slot == 53) {
                    submissions.openMenu(page + 1, player);
                }
                else if (slot == 45 && page > 1) {
                    submissions.openMenu(page - 1, player);
                }
                else if (slot < 45) {
                    for (Submitter submitter : submissions.getSubmitters()) {
                        if (submitter.getName().equals(name)) {
                            submitter.openMenu(1, Bukkit.getPlayer(viewer));
                            return;
                        }
                    }
                }
            }
            catch (SubmissionsNotLoadedException e) {
                player.sendMessage(e.getMessage());
            }

            if (!name.equals(" ") && (slot < 46 || slot == 53 || (slot == 46 && page > 1))) {
                event.setWillDestroy(true);
            }
        }));

        ItemStack[] itemStacks = new ItemStack[54];
        try {
            List<ItemStack> list = ServerSaturday.instance().getSubmissions().getSubmitters().stream().map(Submitter::getMenuRepresentation).collect(Collectors.toList());
            for (int x = 0; x < 54; x++) {
                int subListPosition = x + (page - 1) * 45;
                if (x < 45 && list.size() > subListPosition) {
                    itemStacks[x] = list.get(subListPosition);
                }
            }

            inv.setContents(itemStacks);
        }
        catch (SubmissionsNotLoadedException e) {
            ServerSaturday.instance().getLogger().warning("An error occurred while trying to complete this action.");
        }

        setOption(45, new ItemStack(Material.ARROW), "Page " + (page - 1));
        setOption(53, new ItemStack(Material.ARROW), "Page " + (page + 1));
    }
}
