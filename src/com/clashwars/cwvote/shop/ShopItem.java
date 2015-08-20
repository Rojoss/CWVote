package com.clashwars.cwvote.shop;

import com.clashwars.cwcore.debug.Debug;
import com.clashwars.cwcore.helpers.CWItem;
import com.clashwars.cwcore.utils.CWUtil;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShopItem {

    public static List<ShopItem> shopItems = new ArrayList<ShopItem>();

    public String server;
    public String name;
    public int price;
    public CWItem item;
    public String[] description;

    public ShopItem(String name, int price, CWItem item, String[] description) {
        this.name = name;
        this.price = price;
        this.item = item;
        this.description = description;
        this.server = "general";
        ShopItem shopItem = getItem(getItem());
        if (shopItem != null) {
            shopItems.remove(shopItem);
        }
        shopItems.add(this);
    }

    public ShopItem(String name, int price, CWItem item, String[] description, String server) {
        this.name = name;
        this.price = price;
        this.item = item;
        this.description = description;
        this.server = server;
        ShopItem shopItem = getItem(getItem());
        if (shopItem != null) {
            shopItems.remove(shopItem);
        }
        shopItems.add(this);
    }

    public static ShopItem getItem(CWItem item) {
        for (ShopItem shopItem : shopItems) {
            if (!CWUtil.removeColour(shopItem.name).equalsIgnoreCase(CWUtil.removeColour(item.getName()))) {
                continue;
            }
            if (shopItem.item.getType() != item.getType()) {
                continue;
            }
            if (shopItem.item.getData().getData() != item.getData().getData()) {
                continue;
            }
            return shopItem;
        }
        return null;
    }

    public String[] getLore() {
        return CWUtil.concat(new String[] {"&a&lTokens&8: &6&l" + price, "&8&l-- -- -- -- -- -- -- --"}, description);
    }

    public CWItem getItem() {
        return item.setName(name).setLore(getLore()).hideTooltips();
    }
}
