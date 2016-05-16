package com.campmongoose.serversaturday.common.menu;

import com.campmongoose.serversaturday.common.menu.AbstractMenu.ClickEventHandler;

import java.util.UUID;

@SuppressWarnings("unused")
public abstract class AbstractMenu<C, I, H extends ClickEventHandler, L, Q, S>
{
    protected final H handler;
    protected I inv;

    protected AbstractMenu(I inv, H handler)
    {
        this.inv = inv;
        this.handler = handler;
    }

    protected abstract void destroy();

    public abstract void onClick(L event);

    public abstract void onClose(C event);

    public abstract void onQuit(Q event);

    public abstract void open(UUID uuid);

    protected abstract void setOption(int slot, S itemStack);

    protected abstract void setOption(int slot, S itemStack, String name);

    protected abstract void setOption(int slot, S itemStack, String name, String... description);

    protected abstract void setOption(int slot, S itemStack, String name, boolean willGlow, String... description);

    @FunctionalInterface
    public interface ClickEventHandler<E extends SSClickEvent<S, P>, S, P>
    {
        void handle(E event);
    }

    public static class SSClickEvent<S, P>
    {
        boolean close = false;
        boolean destroy = false;
        final int slot;
        final S itemStack;
        final P player;

        public SSClickEvent(P player, S itemStack, int slot)
        {
            this.player = player;
            this.itemStack = itemStack;
            this.slot = slot;
        }

        public boolean willClose()
        {
            return close;
        }

        public void setWillClose(boolean close)
        {
            this.close = close;
        }

        public boolean willDestroy()
        {
            return destroy;
        }

        public void setWillDestroy(boolean destroy)
        {
            this.destroy = destroy;
        }

        public int getSlot()
        {
            return slot;
        }

        public S getItem()
        {
            return itemStack;
        }

        public P getPlayer()
        {
            return player;
        }
    }
}
