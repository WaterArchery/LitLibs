package me.waterarchery.litlibs.hooks.price;

import com.earth2me.essentials.Essentials;
import me.waterarchery.litlibs.hooks.PriceHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

public class EssentialsPriceHook implements PriceHook {

    private static EssentialsPriceHook instance = null;

    public static synchronized EssentialsPriceHook getInstance() {
        if (instance == null) instance = new EssentialsPriceHook();

        return instance;
    }

    private EssentialsPriceHook() { }

    public double getPrice(ItemStack item) {
        Essentials essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        BigDecimal price = essentials.getWorth().getPrice(essentials, item);
        if (price == null) {
            return 0;
        }
        else {
            return price.doubleValue() * item.getAmount();
        }
    }

    @Override
    public double getPrice(Player player, ItemStack item) {
        return getPrice(item);
    }

}
