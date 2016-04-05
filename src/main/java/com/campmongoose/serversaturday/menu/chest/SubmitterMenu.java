package com.campmongoose.serversaturday.menu.chest;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class SubmitterMenu extends ChestMenu
{
    public SubmitterMenu(ServerSaturday plugin, Submitter submitter, int page, Inventory inv)
    {
        super(plugin, inv, event ->
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                {
                    ItemStack itemStack = event.getItem();
                    int slot = event.getSlot();
                    Player player = event.getPlayer();
                    if (slot == 53)
                        submitter.openMenu(plugin, page + 1, player);
                    else if (slot == 45 && page > 1)
                        submitter.openMenu(plugin, page - 1, player);
                    else if (slot == 49)
                        plugin.getSubmissions().openMenu(plugin, 1, player);
                    else if (slot < 45)
                    {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        String name = itemStack.getItemMeta().getDisplayName();
                        for (Build build : submitter.getBuilds())
                        {
                            if (build.getName().equals(name))
                            {
                                build.openMenu(plugin, submitter, player);
                                return;
                            }
                        }
                    }

                    if (slot < 46 || slot == 49 || slot == 53 || (slot == 46 && page > 1))
                        event.setWillDestroy(true);
                }));

        ItemStack[] itemStacks = new ItemStack[54];
        List<ItemStack> list = submitter.getBuilds().stream().map(build -> build.getMenuRepresentation(submitter)).collect(Collectors.toList());
        for (int x = 0; x < 54; x++)
        {
            int subListPosition = x + (page - 1) * 45;
            if (x < 45 && list.size() > subListPosition)
                itemStacks[x] = list.get(subListPosition);
        }

        inv.setContents(itemStacks);
        setOption(45, new ItemStack(Material.ARROW), "Page " + (page - 1));
        setOption(49, new ItemStack(Material.ARROW), "Back", "Go back to S.S. Submissions");
        setOption(53, new ItemStack(Material.ARROW), "Page " + (page + 1));
    }
}
