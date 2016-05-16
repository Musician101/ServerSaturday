package com.campmongoose.serversaturday.sponge.menu;

import com.campmongoose.serversaturday.common.menu.AbstractMenu;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.menu.AbstractSpongeMenu.SpongeClickEventHandler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.type.OrderedInventory;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class AbstractSpongeMenu extends AbstractMenu<InteractInventoryEvent.Close, OrderedInventory, SpongeClickEventHandler, ClickInventoryEvent, ClientConnectionEvent.Disconnect, ItemStack>
{
    protected AbstractSpongeMenu(OrderedInventory inv, SpongeClickEventHandler handler)
    {
        super(inv, handler);
        Sponge.getEventManager().registerListeners(SpongeServerSaturday.instance(), this);
    }

    protected void setOption(int slot, ItemStack itemStack)
    {
        setOption(slot, itemStack, " ");
    }

    protected void setOption(int slot, ItemStack itemStack, String name)
    {
        setOption(slot, itemStack, name, new String[0]);
    }

    protected void setOption(int slot, ItemStack itemStack, String name, String... description)
    {
        setOption(slot, itemStack, name, false, description);
    }

    protected void setOption(int slot, ItemStack itemStack, String name, boolean willGlow, String... description)
    {
        itemStack.offer(Keys.DISPLAY_NAME, Text.of(name));
        itemStack.offer(Keys.ITEM_LORE, Arrays.asList(description).stream().map(Text::of).collect(Collectors.toList()));
        if (willGlow)
        {
            itemStack.offer(Keys.ITEM_ENCHANTMENTS, Collections.singletonList(new ItemEnchantment(Enchantments.AQUA_AFFINITY, 1)));
            itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
        }

        inv.set(new SlotIndex(slot), itemStack);
    }

    @Override
    protected void destroy()
    {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    @Override
    public void onClick(ClickInventoryEvent event)//NOSONAR
    {
        Optional<Player> playerOptional = event.getCause().first(Player.class);
        if (!playerOptional.isPresent())
            return;

        if (!inv.equals(event.getTargetInventory()))
            return;

        if (event.getTransactions().isEmpty())
            return;

        event.setCancelled(true);
        Player player = playerOptional.get();
        ItemStack itemStack = event.getCursorTransaction().getOriginal().createStack();
        if (itemStack.getItem() == BlockTypes.AIR)
            return;

        SpigotSSClickEvent clickEvent = new SpigotSSClickEvent(player, itemStack);
        handler.handle(clickEvent);
        if (clickEvent.willClose())
            player.closeInventory(Cause.of(NamedCause.source(SpongeServerSaturday.instance())));

        if (clickEvent.willDestroy())
            destroy();
    }

    @Listener
    @Override
    public void onClose(InteractInventoryEvent.Close event)
    {
        Optional<Player> playerOptional = event.getCause().first(Player.class);
        if (!playerOptional.isPresent())
            return;

        OrderedInventory inv = (OrderedInventory) event.getTargetInventory();
        if (inv.equals(this.inv))
            destroy();
    }

    @Listener
    @Override
    public void onQuit(ClientConnectionEvent.Disconnect event)
    {
        Optional<Inventory> invOptional = event.getTargetEntity().getOpenInventory();
        if (!invOptional.isPresent())
            return;

        //noinspection RedundantCast
        if (inv.equals((OrderedInventory) invOptional.get()))
            destroy();
    }

    @Override
    public void open(UUID uuid)
    {
        //noinspection OptionalGetWithoutIsPresent
        Sponge.getServer().getPlayer(uuid).get().openInventory(inv, Cause.of(NamedCause.source(SpongeServerSaturday.instance())));
    }

    public static class SpigotSSClickEvent extends AbstractMenu.SSClickEvent<ItemStack, Player>
    {
        public SpigotSSClickEvent(Player player, ItemStack itemStack)
        {
            super(player, itemStack, 0);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public interface SpongeClickEventHandler extends AbstractMenu.ClickEventHandler<SpigotSSClickEvent, ItemStack, Player>
    {

    }
}
