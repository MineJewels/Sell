package org.minejewels.jewelssell.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class SellWandBreakEvent extends Event {

    private final static HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;

    public SellWandBreakEvent(final Player player) {
        this.player = player;
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
