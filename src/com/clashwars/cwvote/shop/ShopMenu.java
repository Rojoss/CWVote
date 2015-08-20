package com.clashwars.cwvote.shop;

import com.clashwars.cwcore.ItemMenu;
import com.clashwars.cwcore.helpers.CWItem;
import com.clashwars.cwcore.utils.CWUtil;
import com.clashwars.cwvote.CWVote;
import com.clashwars.cwvote.events.ShopItemPurchaseEvent;
import com.clashwars.cwvote.util.Util;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class ShopMenu implements Listener {

    private CWVote cwv;
    public ItemMenu menu;

    public ShopMenu(CWVote cwv) {
        this.cwv = cwv;
        menu = new ItemMenu("vote_shop_menu", 54, CWUtil.integrateColor("&4&lVote Shop"));

        menu.setSlot(new CWItem(Material.PAPER).hideTooltips().setName("&6&lINFORMATION").setLore(new String[] {"&7Here you can spend your vote tokens.",
                "&aHover &7over the items for more &ainfo&7.", "&6Click &7on any of the items to &6purchase &7it.", "&7You will lose the specified amount of tokens.",
                "&7The contents of the store depend..", "&7on the server you're on!"}), 0, null);
        menu.setSlot(new CWItem(Material.REDSTONE_BLOCK).hideTooltips().setName("&4&lCLOSE").setLore(new String[] {"&7Close the shop!"}), 8, null);

        Integer[] dividers = new Integer[] {9,10,11,12,13,14,15,16,17};
        for (int dividerSlot : dividers) {
            menu.setSlot(new CWItem(Material.STAINED_GLASS_PANE, 1, (byte)15).hideTooltips().setName("&8-----").setLore(new String[] {"&7Hover over the paper", "&7for information!"}), dividerSlot, null);
        }
    }

    public void showMenu(Player player) {
        int slot = 18;
        for (ShopItem shopItem : ShopItem.shopItems) {
            if (!shopItem.server.equalsIgnoreCase("general") && !shopItem.server.equalsIgnoreCase(cwv.getCfg().SERVER)) {
                continue;
            }
            menu.setSlot(shopItem.getItem(), slot, null);
            slot++;
        }

        player.closeInventory();
        menu.show(player);

        int tokens = cwv.tm.getTokens(player);
        if (tokens < 0) {
            menu.setSlot(new CWItem(Material.NAME_TAG).setName("&4&lNo tokens").setLore(new String[] {"&b/vote &7to earn tokens!"}).hideTooltips().makeGlowing(), 4, player);
        } else {
            menu.setSlot(new CWItem(Material.NAME_TAG).setName("&a&lTokens&8&l: &6&l" + tokens).hideTooltips().makeGlowing(), 4, player);
        }
    }

    @EventHandler
    private void menuClick(ItemMenu.ItemMenuClickEvent event) {
        Long t = System.currentTimeMillis();
        if (menu == null) {
            return;
        }
        if (!event.getItemMenu().getName().equals(menu.getName())) {
            return;
        }
        if (event.getItemMenu().getID() != menu.getID()) {
            return;
        }

        Player player = (Player)event.getWhoClicked();
        CWItem item = new CWItem(event.getCurrentItem());

        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        if (event.getRawSlot() > menu.getSize()) {
            return;
        }

        event.setCancelled(true);
        if (event.getRawSlot() == 8) {
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 2);
            player.closeInventory();
            return;
        }

        if (event.getRawSlot() == 0 || event.getRawSlot() == 4) {
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 2);
            player.closeInventory();
            player.performCommand("vote");
            return;
        }

        if (event.getRawSlot() < 18) {
            return;
        }

        ShopItem shopItem = ShopItem.getItem(item);
        if (shopItem == null) {
            player.sendMessage(Util.formatMsg("&cInvalid item!"));
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 0.5f, 2);
            return;
        }

        int tokens = cwv.tm.getTokens(player);
        if (tokens < shopItem.price) {
            player.sendMessage(Util.formatMsg("&cNot enough tokens to purchase this!"));
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 0.5f, 2);
            return;
        }

        ShopItemPurchaseEvent shopEvent = new ShopItemPurchaseEvent(player, shopItem);
        cwv.getServer().getPluginManager().callEvent(shopEvent);

        if (shopEvent.isCancelled()) {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 0.5f, 2);
        } else {
            cwv.tm.setTokens(player, tokens - shopItem.price);
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 2);
        }
    }


}
