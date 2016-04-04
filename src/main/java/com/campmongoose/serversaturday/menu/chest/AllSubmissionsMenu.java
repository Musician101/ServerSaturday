package com.campmongoose.serversaturday.menu.chest;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Submitter;
import com.campmongoose.serversaturday.util.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AllSubmissionsMenu extends ChestMenu
{
    public AllSubmissionsMenu(ServerSaturday plugin, int page)
    {
        super(plugin, Bukkit.createInventory(null, 54, "All S.S. Submissions"), event ->
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                {
                    ItemStack itemStack = event.getItem();
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    int slot = event.getSlot();
                    Player player = event.getPlayer();
                    String itemName = itemStack.getType() == Material.WRITTEN_BOOK ? ((BookMeta) itemMeta).getTitle() : itemMeta.getDisplayName();
                    String submitterName;
                    UUID uuid = player.getUniqueId();
                    if (itemStack.getType() == Material.WRITTEN_BOOK)
                    {
                        BookMeta bookMeta = (BookMeta) itemMeta;
                        itemName = bookMeta.getTitle();
                        submitterName = bookMeta.getAuthor();
                        Submitter submitter = null;
                        try
                        {
                            submitter = plugin.getSubmissions().getSubmitter(UUIDUtils.getUUIDOf(submitterName));
                        }
                        catch (IOException e)
                        {
                            for (Submitter s : plugin.getSubmissions().getSubmitters())
                                if (submitterName.equals(s.getName()))
                                    submitter = s;
                        }

                        if (submitter == null)
                            return;

                        if (slot < 45)
                            submitter.getBuild(itemName).openMenu(plugin, submitter, player);
                    }
                    else
                    {
                        itemName = itemMeta.getDisplayName();
                        if (slot == 53)
                            new AllSubmissionsMenu(plugin, page + 1).open(player);
                        else if (slot == 45 && page > 1)
                            new AllSubmissionsMenu(plugin, page - 1).open(player);
                    }

                    if (!itemName.equals(" ") && (slot < 46 || slot == 53 || (slot == 46 && page > 1)))
                        event.setWillDestroy(true);
                }));

        ItemStack[] itemStacks = new ItemStack[54];
        List<ItemStack> list = new ArrayList<>();
        plugin.getSubmissions().getSubmitters().forEach(submitter -> list.addAll(submitter.getBuilds().stream().map(build -> build.getMenuRepresentation(submitter)).collect(Collectors.toList())));
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
