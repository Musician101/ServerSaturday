package com.campmongoose.serversaturday.menu.anvil;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.campmongoose.serversaturday.util.UUIDCacheException;
import java.util.UUID;
import net.minecraft.server.v1_11_R1.ChatComponentText;
import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.PacketPlayOutOpenWindow;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NameChangeMenu extends AnvilMenu {

    private final Build build;

    public NameChangeMenu(Build build, UUID viewer) {
        super(event -> {
            Player player = event.getPlayer();
            if (!viewer.equals(player.getUniqueId())) {
                return;
            }

            int slot = event.getSlot();
            if (slot == 2) {
                ItemStack itemStack = event.getItem();
                if (itemStack.getType() != Material.PAPER) {
                    return;
                }

                if (!itemStack.hasItemMeta()) {
                    return;
                }

                ItemMeta itemMeta = itemStack.getItemMeta();
                if (!itemMeta.hasDisplayName()) {
                    return;
                }

                String name = itemMeta.getDisplayName();
                try {
                    Submitter submitter = ServerSaturday.instance().getSubmissions().getSubmitter(player.getUniqueId());
                    if (submitter.getBuild(name) != null) {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        return;
                    }

                    submitter.updateBuildName(build, name);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(ServerSaturday.instance(), () -> build.openMenu(submitter, Bukkit.getPlayer(viewer)));
                }
                catch (UUIDCacheException e) {
                    player.sendMessage("An error occurred while trying to complete this action.");
                }
            }
            else {
                event.setWillClose(false);
                event.setWillDestroy(false);
            }
        });

        this.build = build;
    }

    @Override
    public void open(Player player) {
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
