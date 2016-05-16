package com.campmongoose.serversaturday.sponge.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.uuid.UUIDUtils;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.custom.CustomInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.translation.FixedTranslation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AllSubmissionsMenu extends ChestMenu
{
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public AllSubmissionsMenu(int page, UUID viewer)//NOSONAR
    {
        super(CustomInventory.builder().size(54).name(new FixedTranslation(MenuText.ALL_SUBMISSIONS)).build(), event ->//NOSONAR
        {
            ItemStack itemStack = event.getItem();
            int slot = event.getSlot();
            Player player = event.getPlayer();
            String itemName = itemStack.get(Keys.DISPLAY_NAME).get().toPlain();
            String submitterName;
            UUID uuid = player.getUniqueId();
            if (!uuid.equals(viewer))
                return;

            if (itemStack.getItem() == ItemTypes.BOOK)
            {
                submitterName = itemStack.get(Keys.ITEM_LORE).get().get(0).toPlain();
                SpongeSubmitter submitter = null;
                try
                {
                    submitter = SpongeServerSaturday.instance().getSubmissions().getSubmitter(UUIDUtils.getUUIDOf(submitterName));
                }
                catch (IOException e)//NOSONAR
                {
                    for (SpongeSubmitter s : SpongeServerSaturday.instance().getSubmissions().getSubmitters())
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

        List<ItemStack> list = new ArrayList<>();
        for (SpongeSubmitter submitter : SpongeServerSaturday.instance().getSubmissions().getSubmitters())
        {
            for (SpongeBuild build : submitter.getBuilds())
            {
                ItemStack itemStack = ItemStack.of(ItemTypes.BOOK, 1);
                itemStack.offer(Keys.DISPLAY_NAME, Text.of(build.getName()));
                itemStack.offer(Keys.ITEM_LORE, Collections.singletonList(Text.of(submitter.getName())));
                if (build.submitted() && !build.featured())
                {
                    itemStack.offer(Keys.ITEM_ENCHANTMENTS, Collections.singletonList(new ItemEnchantment(Enchantments.AQUA_AFFINITY, 1)));
                    itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
                }

                list.add(itemStack);
            }
        }

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
