package me.waterarchery.litlibs.handlers;

import com.cryptomorin.xseries.XMaterial;
import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.hooks.other.NBTAPIHook;
import me.waterarchery.litlibs.inventory.ActionType;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.Optional;

public class InventoryHandler {

    private final LitLibs litLibs;

    public InventoryHandler(LitLibs litLibs) { this.litLibs = litLibs; }

    @Deprecated
    public void fillGUI(Inventory inventory, String path, ConfigManager file) {
        FileConfiguration yaml = file.getYml();

        boolean isFillEnabled = yaml.getBoolean(path + ".fillMenu.enabled", false);
        if (isFillEnabled) {
            String rawMaterial = yaml.getString(path + ".fillMenu.fillItem", "STONE");;
            String itemName = yaml.getString(path + ".fillMenu.name", "§7");;
            int modelData = yaml.getInt(path + ".fillMenu.customModelData", -1);;
            Optional<XMaterial> optMaterial = XMaterial.matchXMaterial(rawMaterial);
            XMaterial material = optMaterial.orElse(XMaterial.STONE);
            ItemStack itemStack = material.parseItem();
            itemStack = setGUIAction(itemStack, "none", ActionType.FILL, file.getName());

            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(itemName.replace("&", "§"));
            if (modelData != -1)
                meta.setCustomModelData(modelData);
            itemStack.setItemMeta(meta);

            for (int slot = 0; slot < inventory.getSize(); slot++) {
                ItemStack item = inventory.getItem(slot);
                if (item == null || item.getType() == Material.AIR)
                    inventory.setItem(slot, itemStack);
            }
        }
    }

    public ItemStack setGUIAction(ItemStack itemStack, String action, ActionType type, String fileName) {
        NBTAPIHook nbtapiHook = litLibs.getNBTAPIHook();
        return nbtapiHook.setGUIAction(action, itemStack, fileName, type);
    }

    public ItemStack parseItemStack(@Nullable String rawMaterial, OfflinePlayer player) {
        if (rawMaterial == null) return new ItemStack(Material.STONE);

        CacheHandler headCache = CacheHandler.getInstance();
        if (rawMaterial.contains("HEAD-")) {
            rawMaterial = rawMaterial.replace("HEAD-", "");
            return headCache.getCachedItem(rawMaterial);
        }
        else if (rawMaterial.contains("PLAYER")) {
            return headCache.getCachedItem(player.getUniqueId().toString());
        }
        else {
            Optional<XMaterial> optMaterial = XMaterial.matchXMaterial(rawMaterial);
            XMaterial material = optMaterial.orElse(XMaterial.STONE);
            return material.parseItem();
        }
    }

    public ItemStack parseItemStack(@Nullable String rawMaterial) {
        return parseItemStack(rawMaterial, null);
    }

}