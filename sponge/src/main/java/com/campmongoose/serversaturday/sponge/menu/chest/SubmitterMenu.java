package com.campmongoose.serversaturday.sponge.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.OrderedInventory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SubmitterMenu extends ChestMenu
{
    public SubmitterMenu(SpongeSubmitter submitter, int page, OrderedInventory inv)//NOSONAR
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
                SpongeServerSaturday.instance().getSubmissions().openMenu(1, uuid);
            else if (slot < 45)
            {
                @SuppressWarnings("OptionalGetWithoutIsPresent")
                String name = itemStack.get(Keys.DISPLAY_NAME).get().toPlain();
                for (SpongeBuild build : submitter.getBuilds())
                {
                    if (build.getName().equals(name))
                    {
                        build.openMenu(submitter, uuid);
                        return;
                    }
                }
            }
        });

        List<ItemStack> list = submitter.getBuilds().stream().map(build -> build.getMenuRepresentation(submitter)).collect(Collectors.toList());
        for (int x = 0; x < 54; x++)
        {
            int subListPosition = x + (page - 1) * 45;
            if (x < 45 && list.size() > subListPosition)
                setOption(x, list.get(subListPosition));
        }

        setOption(45, ItemStack.of(ItemTypes.ARROW, 1), MenuText.page(page - 1));
        setOption(49, ItemStack.of(ItemTypes.ARROW, 1), MenuText.BACK);
        setOption(53, ItemStack.of(ItemTypes.ARROW, 1), MenuText.page(page + 1));
    }
}
