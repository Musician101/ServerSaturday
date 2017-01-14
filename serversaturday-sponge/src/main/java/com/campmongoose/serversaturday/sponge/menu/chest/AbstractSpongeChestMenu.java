package com.campmongoose.serversaturday.sponge.menu.chest;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.menu.AbstractChestMenu;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryCapacity;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.text.Text;

//TODO create paged Menu
public abstract class AbstractSpongeChestMenu extends AbstractChestMenu<Inventory, AbstractSpongeChestMenu, Player, ItemStack>
{
    private final Map<Integer, ItemStack> slots = new HashMap<>();

    public AbstractSpongeChestMenu(@Nonnull String name, int size, @Nonnull Player player, @Nullable AbstractSpongeChestMenu prevMenu)
    {
        super(parseInventory(name, size), player, prevMenu);
        open();
    }

    @Override
    protected void set(int slot, @Nonnull ItemStack itemStack) {
        inventory.query(new SlotIndex(slot)).set(itemStack);
        slots.put(slot, itemStack);
    }

    @Override
    protected void set(int slot, @Nonnull ItemStack itemStack, @Nonnull Consumer<Player> consumer)
    {
        set(slot, itemStack);
        buttons.put(slot, consumer);
    }

    protected void close()
    {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    public void onClick(ClickInventoryEvent event, @First Player player)
    {
        Container container = event.getTargetInventory();
        if (isSameInventory(container, player)) {
            event.setCancelled(true);
            if (event instanceof ClickInventoryEvent.Creative || event instanceof ClickInventoryEvent.Drag || event instanceof ClickInventoryEvent.Shift || event instanceof ClickInventoryEvent.NumberPress || event instanceof ClickInventoryEvent.Drop) {
                player.sendMessage(Text.of("That click type is not supported."));
            }
            else {
                BiMap<ItemStack, Integer> inverseSlots = HashBiMap.create(slots).inverse();
                ItemStack itemStack = event.getCursorTransaction().getFinal().createStack();
                if (inverseSlots.containsKey(itemStack)) {
                    int slot = inverseSlots.get(itemStack);
                    if (buttons.containsKey(slot))
                        buttons.get(slot).accept(player);
                }
            }
        }
    }

    @Listener
    public void onClose(InteractInventoryEvent.Close event, @First Player player)
    {
        if (isSameInventory(event.getTargetInventory(), player))
            close();
    }

    private boolean isSameInventory(Inventory inventory, Player player) {
        return inventory.getName().equals(this.inventory.getName()) && Reference.ID.equals(inventory.getPlugin().getId()) && player.getUniqueId().equals(this.player.getUniqueId());
    }

    @Override
    public void open()
    {
        inventory.clear();
        build();
        for (int i = 0; i < inventory.capacity(); i++) {
            if (slots.containsKey(i))
                inventory.query(new SlotIndex(i)).set(slots.getOrDefault(i, ItemStack.of(ItemTypes.NONE, 1)));
        }

        player.openInventory(inventory, Cause.of(NamedCause.source(SpongeServerSaturday.instance())));
        Sponge.getEventManager().registerListeners(SpongeServerSaturday.instance(), this);
    }

    private static Inventory parseInventory(String name, int size) {
        InventoryArchetype.Builder builder = InventoryArchetype.builder().property(new InventoryCapacity(size)).title(Text.of(name));
        for (int i = 0; i < size; i++) {
            builder.with(InventoryArchetype.builder().from(InventoryArchetypes.SLOT).property(new SlotIndex(i)).build("minecraft:slot" + i, "Slot"));
        }

        return Inventory.builder().of(builder.build(Reference.ID + ":" + name.replace("\\s", "_").toLowerCase(), name)).build(SpongeServerSaturday.instance());
    }
}
