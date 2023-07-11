package org.minejewels.jewelssell.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SellWandSellEvent extends Event {

    private final static HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final double amount;

    public SellWandSellEvent(final Player player, final double amount) {
        this.player = player;
        this.amount = amount;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
