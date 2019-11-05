package com.campmongoose.serversaturday.sponge.gui.anvil;

import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import net.minecraft.block.BlockAnvil.Anvil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

public class AnvilGUI {

    public AnvilGUI(@Nonnull Player player, @Nonnull BiFunction<EntityPlayer, String, String> biFunction) {
        EntityPlayer entityPlayer = (EntityPlayer) player;
        entityPlayer.displayGui(new SSAnvil(entityPlayer, biFunction));
    }

    static class SSAnvil extends Anvil {

        private final BiFunction<EntityPlayer, String, String> biFunction;

        public SSAnvil(EntityPlayer player, BiFunction<EntityPlayer, String, String> biFunction) {
            super(player.getEntityWorld(), player.getPosition());
            this.biFunction = biFunction;
        }

        @Nonnull
        @Override
        public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer playerIn) {
            return new SSContainerRepair(playerInventory, playerIn, biFunction);
        }
    }

    static class SSContainerRepair extends ContainerRepair {

        private final BiFunction<EntityPlayer, String, String> biFunction;

        public SSContainerRepair(InventoryPlayer inventoryPlayer, EntityPlayer player, BiFunction<EntityPlayer, String, String> biFunction) {
            super(inventoryPlayer, player.getEntityWorld(), player.getPosition(), player);
            this.biFunction = biFunction;
            ItemStack itemStack = new ItemStack(Items.PAPER);
            itemStack.setStackDisplayName("Rename Me!");
            putStackInSlot(0, itemStack);
        }

        @Override
        public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
            return true;
        }

        @Override
        public void onContainerClosed(EntityPlayer playerIn) {

        }

        @Nonnull
        @Override
        public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
            if (slotId == 2) {
                Slot slot = getSlot(2);
                if (slot.getHasStack()) {
                    ItemStack itemStack = slot.getStack();
                    String ret = biFunction.apply(player, itemStack.getDisplayName());
                    if (ret != null) {
                        getSlot(0).getStack().setStackDisplayName(ret);
                    }
                    else {
                        Sponge.getServer().getPlayer(player.getUniqueID()).ifPresent(Player::closeInventory);
                    }
                }
            }

            return ItemStack.EMPTY;
        }
    }
}
