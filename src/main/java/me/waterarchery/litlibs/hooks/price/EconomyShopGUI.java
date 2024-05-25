package me.waterarchery.litlibs.hooks.price;

import me.gypopo.economyshopgui.api.EconomyShopGUIHook;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.waterarchery.litlibs.hooks.PriceHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EconomyShopGUI implements PriceHook {


    private static EconomyShopGUI instance = null;

    public static synchronized EconomyShopGUI getInstance() {
        if (instance == null)
            instance = new EconomyShopGUI();

        return instance;
    }

    private EconomyShopGUI() { }

    @Override
    public double getPrice(ItemStack item) {
        ShopItem shopItem = EconomyShopGUIHook.getShopItem(item);
        if (shopItem != null && EconomyShopGUIHook.getItemSellPrice(shopItem, item) != null) {
            return EconomyShopGUIHook.getItemSellPrice(shopItem, item);
        }
        else {
            return - 1;
        }
    }

    @Override
    public double getPrice(Player player, ItemStack item) {
        ShopItem shopItem = EconomyShopGUIHook.getShopItem(item);
        if (shopItem != null && EconomyShopGUIHook.getItemSellPrice(shopItem, item, player) != null && !EconomyShopGUIHook.getItemSellPrice(shopItem, item, player).isNaN()) {
            return EconomyShopGUIHook.getItemSellPrice(shopItem, item, player);
        }
        else {
            return - 1;
        }
    }
}
