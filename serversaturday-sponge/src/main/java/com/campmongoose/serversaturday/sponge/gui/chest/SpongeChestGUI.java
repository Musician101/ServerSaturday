package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.gui.chest.ChestGUI;
import com.campmongoose.serversaturday.common.gui.chest.GUIButton;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.data.key.SSKeys;
import java.util.List;
import javax.annotation.Nonnull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent.Close;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryCapacity;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public final class SpongeChestGUI extends ChestGUI<Class<? extends ClickInventoryEvent>, Inventory, Player, ItemStack, Text> {

    public SpongeChestGUI(@Nonnull Player player, @Nonnull Text name, int size, @Nonnull List<GUIButton<Class<? extends ClickInventoryEvent>, Player, ItemStack>> buttons, int page, boolean manualOpen) {
        super(parseInventory(name, size), name, player, buttons, page, manualOpen);
    }

    public static SpongeChestGUIBuilder builder() {
        return new SpongeChestGUIBuilder();
    }

    private static Inventory parseInventory(@Nonnull Text name, int size) {
        InventoryArchetype.Builder builder = InventoryArchetype.builder().property(InventoryCapacity.of(size)).title(Text.of(name));
        for (int i = 0; i < size; i++) {
            builder.with(InventoryArchetype.builder().from(InventoryArchetypes.SLOT).property(SlotIndex.of(i)).build("minecraft:slot" + i, "Slot"));
        }

        String plainName = TextSerializers.PLAIN.serialize(name);
        return Inventory.builder().of(builder.build(Reference.ID + ":" + plainName.replace("\\s", "_").toLowerCase(), plainName)).build(SpongeServerSaturday.instance().get());
    }

    @Override
    public final void close() {
        player.closeInventory();
    }

    private boolean isSameInventory(@Nonnull Inventory inventory, @Nonnull Player player) {
        return inventory.getName().equals(this.inventory.getName()) && player.getUniqueId().equals(this.player.getUniqueId());
    }

    @Listener
    public void onInventoryClick(ClickInventoryEvent event, @First Player player) {
        Container container = event.getTargetInventory();
        if (isSameInventory(container, player)) {
            event.setCancelled(true);
            int slot = event.getCursorTransaction().getFinal().createStack().get(SSKeys.SLOT).orElse(-1);
            buttons.stream().filter(button -> button.getSlot() == slot && button.getClickType() == event.getClass()).findFirst().flatMap(GUIButton::getAction).ifPresent(action -> action.accept(player));
        }
    }

    @Listener
    public final void onInventoryClose(Close event, @First Player player) {
        if (event.getTargetInventory().getName().equals(inventory.getName()) && event.getTargetInventory().getViewers().stream().anyMatch(p -> p.getUniqueId().equals(player.getUniqueId()))) {
            Sponge.getEventManager().unregisterListeners(this);
        }
    }

    @Override
    public void open() {
        SpongeServerSaturday.instance().ifPresent(plugin -> Task.builder().execute(() -> {
            inventory.clear();
            buttons.forEach(button -> inventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(button.getSlot()))).set(button.getItemStack()));
            player.openInventory(inventory);
            Sponge.getEventManager().registerListeners(plugin, this);
        }).delayTicks(1L).submit(plugin));
    }
}
