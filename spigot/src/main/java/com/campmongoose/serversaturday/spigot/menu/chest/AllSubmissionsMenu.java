package com.campmongoose.serversaturday.spigot.menu.chest;

import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.uuid.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AllSubmissionsMenu extends ChestMenu
{
    public AllSubmissionsMenu(int page, UUID viewer)//NOSONAR
    {
        super(Bukkit.createInventory(null, 54, MenuText.ALL_SUBMISSIONS), event ->//NOSONAR
        {
            ItemStack itemStack = event.getItem();
            ItemMeta itemMeta = itemStack.getItemMeta();
            int slot = event.getSlot();
            Player player = event.getPlayer();
            String itemName = itemMeta.getDisplayName();
            String submitterName;
            UUID uuid = player.getUniqueId();
            if (!uuid.equals(viewer))
                return;

            if (itemStack.getType() == Material.BOOK)
            {
                submitterName = itemMeta.getLore().get(0);
                SpigotSubmitter submitter = null;
                try
                {
                    submitter = SpigotServerSaturday.getInstance().getSubmissions().getSubmitter(UUIDUtils.getUUIDOf(submitterName));
                }
                catch (IOException e)//NOSONAR
                {
                    for (SpigotSubmitter s : SpigotServerSaturday.getInstance().getSubmissions().getSubmitters())
                        if (submitterName.equals(s.getName()))
                            submitter = s;
                }

                if (submitter == null)
                    return;

                if (slot < 45)
                    submitter.getBuild(itemName).openMenu(submitter, uuid);
            }
            else
            {
                if (slot == 53)
                    new AllSubmissionsMenu(page + 1, uuid).open(uuid);
                else if (slot == 45 && page > 1)
                    new AllSubmissionsMenu(page - 1, uuid).open(uuid);
            }
        });

        ItemStack[] itemStacks = new ItemStack[54];
        List<ItemStack> list = new ArrayList<>();
        for (SpigotSubmitter submitter : SpigotServerSaturday.getInstance().getSubmissions().getSubmitters())
        {
            for (SpigotBuild build : submitter.getBuilds())
            {
                ItemStack itemStack = new ItemStack(Material.BOOK);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(build.getName());
                itemMeta.setLore(Collections.singletonList(submitter.getName()));
                if (build.submitted() && !build.featured())
                {
                    itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }

                itemStack.setItemMeta(itemMeta);
                list.add(itemStack);
            }
        }

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
