package com.campmongoose.serversaturday.sponge.menu.anvil;

import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.UUID;

public class NameChangeMenu extends AnvilMenu
{
    public NameChangeMenu(SpongeBuild build, UUID viewer)//NOSONAR
    {
        super(event ->//NOSONAR
        {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();
            if (!viewer.equals(uuid))
                return;

            int slot = event.getSlot();
            if (slot == 2)
            {
                ItemStack itemStack = event.getItem();
                if (itemStack.getItem() != ItemTypes.PAPER)
                    return;

                Optional<Text> textOptional = itemStack.get(Keys.DISPLAY_NAME);
                if (!textOptional.isPresent())
                    return;

                String name = textOptional.get().toPlain();
                SpongeSubmitter submitter = SpongeServerSaturday.instance().getSubmissions().getSubmitter(uuid);
                if (submitter.getBuild(name) != null)
                    return;

                submitter.updateBuildName(build, name);
                build.openMenu(submitter, uuid);
            }
        });
    }
}
