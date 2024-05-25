package me.waterarchery.litlibs.hooks;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PriceHook {

    double getPrice(ItemStack item);

    double getPrice(Player player, ItemStack item);

}
