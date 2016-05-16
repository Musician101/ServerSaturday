package com.campmongoose.serversaturday.spigot.menu.anvil;

import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import net.minecraft.server.v1_9_R1.ChatComponentText;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.PacketPlayOutOpenWindow;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class ResourcePackChangeMenu extends AnvilMenu
{
    private final SpigotBuild build;

    public ResourcePackChangeMenu(SpigotBuild build, UUID viewer)
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
                if (itemStack.getType() != Material.PAINTING)
                    return;

                if (!itemStack.hasItemMeta())
                    return;

                ItemMeta itemMeta = itemStack.getItemMeta();
                if (!itemMeta.hasDisplayName())
                    return;

                String name = itemMeta.getDisplayName();
                SpigotSubmitter submitter = SpigotServerSaturday.getInstance().getSubmissions().getSubmitter(uuid);
                submitter.updateBuildResourcePack(build, name);
                submitter.getBuild(build.getName()).openMenu(submitter, uuid);
            }
        });

        this.build = build;
    }

    @Override
    public void open(UUID uuid)
    {
        EntityPlayer ep = ((CraftPlayer) Bukkit.getPlayer(uuid)).getHandle();
        AnvilContainer container = new AnvilContainer(ep);
        inv = container.getBukkitView().getTopInventory();
        setOption(0, new ItemStack(Material.PAINTING), build.getResourcePack(), MenuText.RENAME_ME, MenuText.CLICK_TO_CONFIRM);
        int c = ep.nextContainerCounter();
        ep.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatComponentText("Repairing")));
        ep.activeContainer = container;
        ep.activeContainer.windowId = c;
        ep.activeContainer.addSlotListener(ep);
    }
}
