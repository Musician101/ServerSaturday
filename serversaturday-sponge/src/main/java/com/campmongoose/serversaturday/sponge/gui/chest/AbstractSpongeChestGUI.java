package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.gui.AbstractChestGUI;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryCapacity;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

//TODO create paged Menu
public abstract class AbstractSpongeChestGUI extends AbstractChestGUI<Text, Inventory, AbstractSpongeChestGUI, Player, ItemStack, ItemType> {

    private final Map<Integer, ItemStack> slots = new HashMap<>();

    public AbstractSpongeChestGUI(@Nonnull String name, int size, @Nonnull Player player, @Nullable AbstractSpongeChestGUI prevMenu) {
        this(name, size, player, prevMenu, false);
    }

    public AbstractSpongeChestGUI(@Nonnull String name, int size, @Nonnull Player player, @Nullable AbstractSpongeChestGUI prevMenu, boolean manualOpen) {
        super(parseInventory(name, size), player, prevMenu);
        open();
    }

    private static Inventory parseInventory(String name, int size) {
        InventoryArchetype.Builder builder = InventoryArchetype.builder().property(new InventoryCapacity(size)).title(Text.of(name));
        for (int i = 0; i < size; i++) {
            builder.with(InventoryArchetype.builder().from(InventoryArchetypes.SLOT).property(new SlotIndex(i)).build("minecraft:slot" + i, "Slot"));
        }

        return Inventory.builder().of(builder.build(Reference.ID + ":" + name.replace("\\s", "_").toLowerCase(), name)).build(SpongeServerSaturday.instance());
    }

    protected void close() {
        Sponge.getEventManager().unregisterListeners(this);
    }

    private boolean isSameInventory(Inventory inventory, Player player) {
        return inventory.getName().equals(this.inventory.getName()) && Reference.ID.equals(inventory.getPlugin().getId()) && player.getUniqueId().equals(this.player.getUniqueId());
    }

    @Listener
    public void onClick(ClickInventoryEvent event, @First Player player) {
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
                    if (buttons.containsKey(slot)) {
                        buttons.get(slot).accept(player);
                    }
                }
            }
        }
    }

    @Listener
    public void onClose(InteractInventoryEvent.Close event, @First Player player) {
        if (isSameInventory(event.getTargetInventory(), player)) {
            close();
        }
    }

    @Override
    public void open() {
        inventory.clear();
        build();
        for (int i = 0; i < inventory.capacity(); i++) {
            if (slots.containsKey(i)) {
                inventory.query(new SlotIndex(i)).set(slots.getOrDefault(i, ItemStack.of(ItemTypes.NONE, 1)));
            }
        }

        player.openInventory(inventory, Cause.of(NamedCause.source(SpongeServerSaturday.instance())));
        Sponge.getEventManager().registerListeners(SpongeServerSaturday.instance(), this);
    }

    @Override
    protected void set(int slot, @Nonnull ItemStack itemStack, @Nonnull Consumer<Player> consumer) {
        set(slot, itemStack);
        buttons.put(slot, consumer);
    }

    @Override
    protected void set(int slot, @Nonnull ItemStack itemStack) {
        inventory.query(new SlotIndex(slot)).set(itemStack);
        slots.put(slot, itemStack);
    }

    @Nonnull
    @Override
    protected ItemStack createItem(@Nonnull ItemType itemType, @Nonnull Text name, @Nonnull Text... description) {
        return ItemStack.builder().itemType(itemType).add(Keys.DISPLAY_NAME, name)
                .add(Keys.ITEM_LORE, Stream.of(description).map(Text::of).collect(Collectors.toList())).build();
    }

    @Override
    protected void setBackButton(int slot, @Nonnull ItemType itemType) {
        ItemStack itemStack = createItem(itemType, Text.builder("Back").color(TextColors.RED).build(), Text.of("Closes this menu and attempts"), Text.of("to back to the previous menu."));
        set(slot, itemStack, player -> player.closeInventory(Cause.of(NamedCause.source(inventory.getPlugin()))));
    }
}
