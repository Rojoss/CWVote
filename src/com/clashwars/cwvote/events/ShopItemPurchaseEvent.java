package com.clashwars.cwvote.events;

import com.clashwars.cwvote.shop.ShopItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ShopItemPurchaseEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private ShopItem shopItem;
    protected  boolean cancelled;

    public ShopItemPurchaseEvent(Player who, ShopItem shopItem) {
        super(who);
        this.shopItem = shopItem;
    }

    public ShopItem getShopItem() {
        return this.shopItem;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
