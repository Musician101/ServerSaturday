package com.campmongoose.serversaturday.menu.chest;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.campmongoose.serversaturday.util.UUIDCacheException;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SubmitterMenu extends ChestMenu {

    public SubmitterMenu(Submitter submitter, int page, Inventory inv) {
        super(inv, event -> Bukkit.getScheduler().scheduleSyncDelayedTask(ServerSaturday.instance(), () -> {
            ItemStack itemStack = event.getItem();
            int slot = event.getSlot();
            Player player = event.getPlayer();
            if (slot == 53) {
                submitter.openMenu(page + 1, player);
            }
            else if (slot == 45 && page > 1) {
                submitter.openMenu(page - 1, player);
            }
            else if (slot == 49) {
                try {
                    ServerSaturday.instance().getSubmissions().openMenu(1, player);
                }
                catch (UUIDCacheException e) {
                    player.sendMessage("An error occurred while trying to complete this action.");
                }
            }
            else if (slot < 45) {
                String name = itemStack.getItemMeta().getDisplayName();
                for (Build build : submitter.getBuilds()) {
                    if (build.getName().equals(name)) {
                        build.openMenu(submitter, player);
                        return;
                    }
                }
            }

            if (slot < 46 || slot == 49 || slot == 53 || (slot == 46 && page > 1)) {
                event.setWillDestroy(true);
            }
        }));

        ItemStack[] itemStacks = new ItemStack[54];
        List<ItemStack> list = submitter.getBuilds().stream().map(build -> build.getMenuRepresentation(submitter)).collect(Collectors.toList());
        for (int x = 0; x < 54; x++) {
            int subListPosition = x + (page - 1) * 45;
            if (x < 45 && list.size() > subListPosition) {
                itemStacks[x] = list.get(subListPosition);
            }
        }

        inv.setContents(itemStacks);
        setOption(45, new ItemStack(Material.ARROW), "Page " + (page - 1));
        setOption(49, new ItemStack(Material.ARROW), "Back", "Go back to S.S. Submissions");
        setOption(53, new ItemStack(Material.ARROW), "Page " + (page + 1));
    }
}
