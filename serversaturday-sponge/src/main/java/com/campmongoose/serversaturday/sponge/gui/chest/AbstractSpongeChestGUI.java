package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.gui.AbstractChestGUI;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.data.InventorySlotData;
import com.campmongoose.serversaturday.sponge.data.SSKeys;
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
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public abstract class AbstractSpongeChestGUI extends AbstractChestGUI<Text, Inventory, AbstractSpongeChestGUI, Player, ItemStack, ItemType> {

    public AbstractSpongeChestGUI(@Nonnull String name, int size, @Nonnull Player player, @Nullable AbstractSpongeChestGUI prevMenu) {
        this(name, size, player, prevMenu, false);
    }

    public AbstractSpongeChestGUI(@Nonnull String name, int size, @Nonnull Player player, @Nullable AbstractSpongeChestGUI prevMenu, boolean manualOpen) {
        super(parseInventory(name, size), player, prevMenu);
        if (!manualOpen) {
            Task.builder().delayTicks(1).execute(this::open).submit(SpongeServerSaturday.instance());
        }
    }

    private static Inventory parseInventory(String name, int size) {
        InventoryArchetype.Builder builder = InventoryArchetype.builder().property(new InventoryCapacity(54)).title(Text.of(name));
        for (int x = 0; x < size; x++) {
            builder.with(InventoryArchetype.builder().with(InventoryArchetypes.SLOT).property(new SlotIndex(x)).build("minecraft:slot" + x, "Slot"));
        }

        return Inventory.builder().of(builder.build(Reference.ID + ":" + name.replace("\\s", "_").toLowerCase(), name)).build(SpongeServerSaturday.instance());
    }

    protected void close() {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Nonnull
    @Override
    protected ItemStack createItem(@Nonnull ItemType itemType, @Nonnull Text name, @Nonnull Text... description) {
        return ItemStack.builder().itemType(itemType).add(Keys.DISPLAY_NAME, name).add(Keys.ITEM_LORE, Stream.of(description).map(Text::of).collect(Collectors.toList())).build();
    }

    private boolean isSameInventory(Inventory inventory, Player player) {
        return inventory.getName().get().equals(this.inventory.getName().get()) && player.getUniqueId().equals(this.player.getUniqueId());
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
                ItemStack itemStack = event.getCursorTransaction().getFinal().createStack();
                if (itemStack.getItem() != ItemTypes.NONE) {
                    itemStack.get(SSKeys.INVENTORY_SLOT).ifPresent(slot -> buttons.getOrDefault(slot, p -> {}).accept(player));
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
        player.openInventory(inventory, generatePluginCause());
        Sponge.getEventManager().registerListeners(SpongeServerSaturday.instance(), this);
    }

    @Override
    public void delayedOpen() {
        Task.builder().name(getClass().getName() + "#delayedOpen()-Task").execute(this::open).delayTicks(1L).submit(SpongeServerSaturday.instance());
    }

    @Override
    protected void set(int slot, @Nonnull ItemStack itemStack) {
        itemStack.offer(new InventorySlotData(slot));
        inventory.query(new SlotIndex(slot)).offer(itemStack);
    }

    @Override
    protected void set(int slot, @Nonnull ItemStack itemStack, @Nonnull Consumer<Player> consumer) {
        set(slot, itemStack);
        buttons.put(slot, consumer);
    }

    @Override
    protected void setBackButton(int slot, @Nonnull ItemType itemType) {
        ItemStack itemStack = createItem(itemType, Text.builder(MenuText.BACK).color(TextColors.RED).build(), MenuText.BACK_DESC.stream().map(Text::of).toArray(Text[]::new));
        set(slot, itemStack, player -> {
            if (prevMenu != null) {
                prevMenu.open();
            }
            else {
                player.closeInventory(generatePluginCause());
            }
        });
    }

    protected Cause generatePluginCause() {
        return Cause.of(NamedCause.source(SpongeServerSaturday.instance()));
    }
}
