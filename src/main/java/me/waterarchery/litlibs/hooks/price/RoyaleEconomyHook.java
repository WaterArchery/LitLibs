package me.waterarchery.litlibs.hooks.price;

import me.qKing12.RoyaleEconomy.RoyaleEconomy;
import me.waterarchery.litlibs.hooks.PriceHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RoyaleEconomyHook implements PriceHook {

    private static RoyaleEconomyHook instance = null;

    public static synchronized RoyaleEconomyHook getInstance() {
        if (instance == null) instance = new RoyaleEconomyHook();

        return instance;
    }

    private RoyaleEconomyHook() {

    }

    @Override
    public double getPrice(ItemStack item) {
        return RoyaleEconomy.apiHandler.shops.getPriceOfItem(item);
    }

    @Override
    public double getPrice(Player player, ItemStack item) {
        return RoyaleEconomy.apiHandler.shops.getPriceOfItem(item);
    }
}
