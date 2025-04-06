package me.waterarchery.litlibs.handlers;

import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class CacheHandler {

    private static CacheHandler cacheHandler;
    private final HashMap<String, ItemStack> cachedPlayerHeads = new HashMap<>();

    public static CacheHandler getInstance() {
        if (cacheHandler == null) cacheHandler = new CacheHandler();
        return cacheHandler;
    }

    private CacheHandler() { }

    public void cacheItem(String key, ItemStack item) {
        if (!cachedPlayerHeads.containsKey(key)) cachedPlayerHeads.put(key, item);
    }

    public ItemStack getCachedItem(String key) {
        ItemStack item = cachedPlayerHeads.get(key);

        if (item == null) {
            try {
                item = XSkull.createItem()
                        .profile(Profileable.detect(key))
                        .apply();
                cachedPlayerHeads.put(key, item);
            }
            catch (Exception e) {
                return new ItemStack(Material.PLAYER_HEAD);
            }
        }

        return item;
    }

}
