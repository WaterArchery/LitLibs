package me.waterarchery.litlibs.hooks.price;

import com.artillexstudios.axgens.api.AxGensAPI;
import me.waterarchery.litlibs.hooks.PriceHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AxGensPriceHook implements PriceHook {


    private static AxGensPriceHook instance = null;

    public static synchronized AxGensPriceHook getInstance() {
        if (instance == null)
            instance = new AxGensPriceHook();

        return instance;
    }

    private AxGensPriceHook() { }

    @Override
    public double getPrice(ItemStack item) {
        return AxGensAPI.getShopPrices().getPrice(item);
    }

    @Override
    public double getPrice(Player player, ItemStack item) {
        return AxGensAPI.getShopPrices().getPrice(player, item);
    }

}
