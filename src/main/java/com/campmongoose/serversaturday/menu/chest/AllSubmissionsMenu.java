package com.campmongoose.serversaturday.menu.chest;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.campmongoose.serversaturday.util.UUIDCacheException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AllSubmissionsMenu extends ChestMenu {

    public AllSubmissionsMenu(int page) {
        super(Bukkit.createInventory(null, 54, "All S.S. Submissions"), event -> Bukkit.getScheduler().scheduleSyncDelayedTask(ServerSaturday.instance(), () -> {
            ItemStack itemStack = event.getItem();
            ItemMeta itemMeta = itemStack.getItemMeta();
            int slot = event.getSlot();
            Player player = event.getPlayer();
            String itemName = itemMeta.getDisplayName();
            String submitterName;
            if (itemStack.getType() == Material.BOOK) {
                submitterName = itemMeta.getLore().get(0);
                try {
                    Submitter submitter = ServerSaturday.instance().getSubmissions().getSubmitter(ServerSaturday.instance().getUUIDCache().getUUIDOf(submitterName));
                    if (submitter == null) {
                        return;
                    }

                    if (slot < 45) {
                        submitter.getBuild(itemName).openMenu(submitter, player);
                    }
                }
                catch (UUIDCacheException e) {
                    player.sendMessage("An error occurred while trying to complete this action.");
                    return;
                }
            }
            else {
                itemName = itemMeta.getDisplayName();
                if (slot == 53) {
                    new AllSubmissionsMenu(page + 1).open(player);
                }
                else if (slot == 45 && page > 1) {
                    new AllSubmissionsMenu(page - 1).open(player);
                }
            }

            if (!itemName.equals(" ") && (slot < 46 || slot == 53 || (slot == 46 && page > 1))) {
                event.setWillDestroy(true);
            }
        }));

        ItemStack[] itemStacks = new ItemStack[54];
        List<ItemStack> list = new ArrayList<>();
        try {
            for (Submitter submitter : ServerSaturday.instance().getSubmissions().getSubmitters()) {
                for (Build build : submitter.getBuilds()) {
                    ItemStack itemStack = new ItemStack(Material.BOOK);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(build.getName());
                    itemMeta.setLore(Collections.singletonList(submitter.getName()));
                    if (build.submitted() && !build.featured()) {
                        itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }

                    itemStack.setItemMeta(itemMeta);
                    list.add(itemStack);
                }
            }

            for (int x = 0; x < 54; x++) {
                int subListPosition = x + (page - 1) * 45;
                if (x < 45 && list.size() > subListPosition) {
                    itemStacks[x] = list.get(subListPosition);
                }
            }

            inv.setContents(itemStacks);
        }
        catch (UUIDCacheException e) {
            ServerSaturday.instance().getLogger().info("An error occurred while trying to complete this action.");
            return;
        }

        setOption(45, new ItemStack(Material.ARROW), "Page " + (page - 1));
        setOption(53, new ItemStack(Material.ARROW), "Page " + (page + 1));
    }
}
