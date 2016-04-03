package com.campmongoose.serversaturday.menu.chest;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SubmitterMenu extends ChestMenu
{
    public SubmitterMenu(ServerSaturday plugin, Submitter submitter, int page, Inventory inv, UUID viewer)
    {
        super(plugin, inv, event ->
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                {
                    ItemStack itemStack = event.getItem();
                    if (itemStack == null)
                        return;

                    int slot = event.getSlot();
                    Player player = event.getPlayer();
                    if (slot == 53)
                        submitter.getMenu(plugin, page + 1, viewer).open(player);
                    else if (slot == 46 && page > 1)
                        submitter.getMenu(plugin, page - 1, viewer).open(player);
                    else if (slot == 49)
                        plugin.getSubmissions().getMenu(plugin, 1, viewer).open(player);
                    else if (slot < 46)
                    {
                        String name = ((BookMeta) itemStack.getItemMeta()).getTitle();
                        for (Build build : submitter.getBuilds())
                        {
                            if (build.getName().equals(name))
                            {
                                build.getMenu(plugin, submitter, viewer).open(player);
                                return;
                            }
                        }
                    }

                    if (slot < 46 || slot == 49 || slot == 53 || (slot == 46 && page > 1))
                        event.setWillDestroy(true);
                }));

        ItemStack[] itemStacks = new ItemStack[54];
        List<ItemStack> list = submitter.getBuilds().stream().map(Build::getMenuRepresentation).collect(Collectors.toList());
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
