package com.campmongoose.serversaturday.sponge.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.OrderedInventory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SubmissionsMenu extends ChestMenu
{
    public SubmissionsMenu(int page, OrderedInventory inv, UUID viewer)//NOSONAR
    {
        super(inv, event ->//NOSONAR
        {
            ItemStack itemStack = event.getItem();
            Player player = event.getPlayer();
            //noinspection OptionalGetWithoutIsPresent
            String name = itemStack.get(Keys.DISPLAY_NAME).get().toPlain();
            SpongeSubmissions submissions = SpongeServerSaturday.instance().getSubmissions();
            UUID uuid = player.getUniqueId();
            if (!viewer.equals(uuid))
                return;

            if (name.equals(MenuText.page(page + 1)))
                submissions.openMenu(page + 1, uuid);
            else if (name.equals(MenuText.page(page - 1)))
                submissions.openMenu(page - 1, uuid);
            else
            {
                for (SpongeSubmitter submitter : submissions.getSubmitters())
                {
                    if (submitter.getName().equals(name))
                    {
                        submitter.openMenu(1, uuid);
                        return;
                    }
                }
            }
        });

        List<ItemStack> list = SpongeServerSaturday.instance().getSubmissions().getSubmitters().stream().map(SpongeSubmitter::getMenuRepresentation).collect(Collectors.toList());
        for (int x = 0; x < 54; x++)
        {
            int subListPosition = x + (page - 1) * 45;
            if (x < 45 && list.size() > subListPosition)
                setOption(x, list.get(subListPosition));
        }

        setOption(45, ItemStack.of(ItemTypes.ARROW, 1), MenuText.page(page - 1));
        setOption(53, ItemStack.of(ItemTypes.ARROW, 1), MenuText.page(page + 1));
    }
}
