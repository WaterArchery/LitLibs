package me.waterarchery.litlibs.hooks.price;

import me.gypopo.economyshopgui.api.EconomyShopGUIHook;
import me.gypopo.economyshopgui.api.objects.SellPrice;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.util.EconomyType;
import me.waterarchery.litlibs.hooks.PriceHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class EconomyShopGUI implements PriceHook {


    private static EconomyShopGUI instance = null;

    public static synchronized EconomyShopGUI getInstance() {
        if (instance == null)
            instance = new EconomyShopGUI();

        return instance;
    }

    private EconomyShopGUI() {
    }

    @Override
    public double getPrice(ItemStack item) {
        ShopItem shopItem = EconomyShopGUIHook.getShopItem(item);

        if (shopItem != null) {
            Double price = EconomyShopGUIHook.getItemSellPrice(shopItem, item);
            if (price != null) {
                return price;
            }
        }

        return -1;
    }

    @Override
    public double getPrice(Player player, ItemStack item) {
        Optional<SellPrice> optSellPrice = EconomyShopGUIHook.getSellPrice(player, item);
        return optSellPrice.map(sellPrice -> sellPrice.getPrice(EconomyType.getFromString("VAULT"))).orElse((double) -1);
    }
}
