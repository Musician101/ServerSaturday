package com.campmongoose.serversaturday.menu.anvil;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class NameChangeMenu extends AnvilMenu
{
    private final Build build;

    public NameChangeMenu(ServerSaturday plugin, Build build, UUID viewer)
    {
        super(plugin, event ->
        {
            Player player = event.getPlayer();
            if (!viewer.equals(player.getUniqueId()))
                return;

            int slot = event.getSlot();
            if (slot == 2)
            {
                ItemStack itemStack = event.getItem();
                if (itemStack.getType() != Material.PAPER)
                    return;

                if (!itemStack.hasItemMeta())
                    return;

                ItemMeta itemMeta = itemStack.getItemMeta();
                if (!itemMeta.hasDisplayName())
                    return;

                String name = itemMeta.getDisplayName();
                Submitter submitter = plugin.getSubmissions().getSubmitter(player.getUniqueId());
                if (submitter.getBuild(name) == null)
                {
                    event.setWillClose(false);
                    event.setWillDestroy(false);
                    return;
                }

                submitter.updateBuildName(build, name);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> build.openMenu(plugin, submitter, Bukkit.getPlayer(viewer)));
            }
            else
            {
                event.setWillClose(false);
                event.setWillDestroy(false);
            }
        });

        this.build = build;
    }

    @Override
    public void open(Player player)
    {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        AnvilContainer container = new AnvilContainer(ep);
        inv = container.getBukkitView().getTopInventory();
        setOption(0, new ItemStack(Material.PAPER), build.getName(), "Rename me!", "Click the result to confirm.");
        int c = ep.nextContainerCounter();
        ep.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatComponentText("Repairing")));
        ep.activeContainer = container;
        ep.activeContainer.windowId = c;
        ep.activeContainer.addSlotListener(ep);
    }
}