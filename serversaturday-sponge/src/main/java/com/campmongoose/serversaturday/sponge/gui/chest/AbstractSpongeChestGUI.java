package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.gui.AbstractChestGUI;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
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

public abstract class AbstractSpongeChestGUI extends AbstractChestGUI<Text, Inventory, String, AbstractSpongeChestGUI, Player, ItemStack, ItemType> {

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
        InventoryArchetype.Builder builder = InventoryArchetype.builder().property(new InventoryCapacity(size)).title(Text.of(name));
        for (int i = 0; i < size; i++) {
            builder.with(InventoryArchetype.builder().from(InventoryArchetypes.SLOT).property(new SlotIndex(i)).build("minecraft:slot" + i, "Slot"));
        }

        return Inventory.builder().of(builder.build(Reference.ID + ":" + name.replace("\\s", "_").toLowerCase(), name)).build(SpongeServerSaturday.instance());
    }

    protected void close() {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Nonnull
    @Override
    protected ItemStack createItem(@Nonnull ItemType itemType, @Nonnull Text name, @Nonnull Text... description) {
        return ItemStack.builder().itemType(itemType).add(Keys.DISPLAY_NAME, name)
                .add(Keys.ITEM_LORE, Stream.of(description).map(Text::of).collect(Collectors.toList())).build();
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
                    String itemName = getItemName(itemStack);
                    if (buttons.containsKey(itemName)) {
                        buttons.get(itemName).accept(player);
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
        player.openInventory(inventory, generatePluginCause());
        Sponge.getEventManager().registerListeners(SpongeServerSaturday.instance(), this);
    }

    @Override
    protected void set(int slot, @Nonnull ItemStack itemStack) {
        set(slot, itemStack, player -> {});
    }

    @Override
    protected void set(int slot, @Nonnull ItemStack itemStack, @Nonnull Consumer<Player> consumer) {
        String itemName = validateItemStackName(itemStack);
        inventory.query(new SlotIndex(slot)).offer(itemStack);
        buttons.put(itemName, consumer);
    }

    private String getItemName(ItemStack itemStack) {
        return itemStack.get(Keys.DISPLAY_NAME).map(Text::toPlain).orElse(itemStack.getTranslation().get());
    }

    private String validateItemStackName(ItemStack itemStack) {
        String itemName = getItemName(itemStack);
        if (buttons.containsKey(itemName)) {
            throw new IllegalArgumentException("A button with that name already exists: " + itemName);
        }

        return itemName;
    }

    @Override
    protected void setBackButton(int slot, @Nonnull ItemType itemType) {
        ItemStack itemStack = createItem(itemType, Text.builder("Back").color(TextColors.RED).build(), Stream.of(MenuText.BACK_DESC).map(Text::of).toArray(Text[]::new));
        set(slot, itemStack, player -> player.closeInventory(generatePluginCause()));
    }

    protected Cause generatePluginCause() {
        return Cause.of(NamedCause.source(SpongeServerSaturday.instance()));
    }
}
