package com.campmongoose.serversaturday.spigot.menu.chest;

import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmissions;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import com.campmongoose.serversaturday.common.Reference.MenuText;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SubmissionsMenu extends ChestMenu
{
    public SubmissionsMenu(int page, Inventory inv, UUID viewer)//NOSONAR
    {
        super(inv, event ->//NOSONAR
        {
            ItemStack itemStack = event.getItem();
            int slot = event.getSlot();
            Player player = event.getPlayer();
            String name = itemStack.getItemMeta().getDisplayName();
            SpigotSubmissions submissions = SpigotServerSaturday.getInstance().getSubmissions();
            UUID uuid = player.getUniqueId();
            if (!viewer.equals(uuid))
                return;

            if (slot == 53)
                submissions.openMenu(page + 1, uuid);
            else if (slot == 45 && page > 1)
                submissions.openMenu(page - 1, uuid);
            else if (slot < 45)
            {
                for (SpigotSubmitter submitter : submissions.getSubmitters())
                {
                    if (submitter.getName().equals(name))
                    {
                        submitter.openMenu(1, uuid);
                        return;
                    }
                }
            }
        });

        ItemStack[] itemStacks = new ItemStack[54];
        List<ItemStack> list = SpigotServerSaturday.getInstance().getSubmissions().getSubmitters().stream().map(SpigotSubmitter::getMenuRepresentation).collect(Collectors.toList());
        for (int x = 0; x < 54; x++)
        {
            int subListPosition = x + (page - 1) * 45;
            if (x < 45 && list.size() > subListPosition)
                itemStacks[x] = list.get(subListPosition);
        }

        inv.setContents(itemStacks);
        setOption(45, new ItemStack(Material.ARROW), MenuText.page(page - 1));
        setOption(53, new ItemStack(Material.ARROW), MenuText.page(page + 1));
    }
}
