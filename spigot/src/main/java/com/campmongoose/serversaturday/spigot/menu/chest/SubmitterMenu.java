package com.campmongoose.serversaturday.spigot.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.spigot.ServerSaturday;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SubmitterMenu extends ChestMenu
{
    public SubmitterMenu(SpigotSubmitter submitter, int page, Inventory inv)//NOSONAR
    {
        super(inv, event ->//NOSONAR
        {
            ItemStack itemStack = event.getItem();
            int slot = event.getSlot();
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();
            if (slot == 53)
                submitter.openMenu(page + 1, player.getUniqueId());
            else if (slot == 45 && page > 1)
                submitter.openMenu(page - 1, player.getUniqueId());
            else if (slot == 49)
                ServerSaturday.getInstance().getSubmissions().openMenu(1, uuid);
            else if (slot < 45)
            {
                String name = itemStack.getItemMeta().getDisplayName();
                for (SpigotBuild build : submitter.getBuilds())
                {
                    if (build.getName().equals(name))
                    {
                        build.openMenu(submitter, uuid);
                        return;
                    }
                }
            }
        });

        ItemStack[] itemStacks = new ItemStack[54];
        List<ItemStack> list = submitter.getBuilds().stream().map(build -> build.getMenuRepresentation(submitter)).collect(Collectors.toList());
        for (int x = 0; x < 54; x++)
        {
            int subListPosition = x + (page - 1) * 45;
            if (x < 45 && list.size() > subListPosition)
                itemStacks[x] = list.get(subListPosition);
        }

        inv.setContents(itemStacks);
        setOption(45, new ItemStack(Material.ARROW), MenuText.page(page - 1));
        setOption(49, new ItemStack(Material.ARROW), MenuText.BACK);
        setOption(53, new ItemStack(Material.ARROW), MenuText.page(page + 1));
    }
}
