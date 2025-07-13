package me.waterarchery.litlibs.hooks.price;

import me.waterarchery.litlibs.hooks.PriceHook;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopGUIPriceHook implements PriceHook {


    private static ShopGUIPriceHook instance = null;

    public static synchronized ShopGUIPriceHook getInstance() {
        if (instance == null) instance = new ShopGUIPriceHook();

        return instance;
    }

    private ShopGUIPriceHook() { }

    @Override
    public double getPrice(ItemStack item) {
        return ShopGuiPlusApi.getItemStackPriceSell(item);
    }

    @Override
    public double getPrice(Player player, ItemStack item) {
        return ShopGuiPlusApi.getItemStackPriceSell(player, item);
    }

}
