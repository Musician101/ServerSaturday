package com.campmongoose.serversaturday.menu.chest;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Submissions;
import com.campmongoose.serversaturday.submission.Submitter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SubmissionsMenu extends ChestMenu
{
    public SubmissionsMenu(ServerSaturday plugin, int page, Inventory inv, UUID viewer)
    {
        super(plugin, inv, event ->
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                {
                    ItemStack itemStack = event.getItem();
                    int slot = event.getSlot();
                    Player player = event.getPlayer();
                    String name = itemStack.getItemMeta().getDisplayName();
                    Submissions submissions = plugin.getSubmissions();
                    if (slot == 53)
                        submissions.openMenu(plugin, page + 1, player);
                    else if (slot == 45 && page > 1)
                        submissions.openMenu(plugin, page - 1, player);
                    else if (slot < 45)
                    {
                        for (Submitter submitter : submissions.getSubmitters())
                        {
                            if (submitter.getName().equals(name))
                            {
                                submitter.openMenu(plugin, 1, Bukkit.getPlayer(viewer));
                                return;
                            }
                        }
                    }

                    if (!name.equals(" ") && (slot < 46 || slot == 53 || (slot == 46 && page > 1)))
                        event.setWillDestroy(true);
                }));

        ItemStack[] itemStacks = new ItemStack[54];
        List<ItemStack> list = plugin.getSubmissions().getSubmitters().stream().map(Submitter::getMenuRepresentation).collect(Collectors.toList());
        for (int x = 0; x < 54; x++)
        {
            int subListPosition = x + (page - 1) * 45;
            if (x < 45 && list.size() > subListPosition)
                itemStacks[x] = list.get(subListPosition);
        }

        inv.setContents(itemStacks);
        setOption(45, new ItemStack(Material.ARROW), "Page " + (page - 1));
        setOption(53, new ItemStack(Material.ARROW), "Page " + (page + 1));
    }
}