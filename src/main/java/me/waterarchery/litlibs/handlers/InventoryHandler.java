package me.waterarchery.litlibs.handlers;

import com.cryptomorin.xseries.XMaterial;
import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.hooks.other.NBTAPIHook;
import me.waterarchery.litlibs.inventory.ActionType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;

public class InventoryHandler {

    private final LitLibs litLibs;

    public InventoryHandler(LitLibs litLibs) { this.litLibs = litLibs; }

    public void fillGUI(Inventory inventory, String path, ConfigManager file) {
        FileConfiguration yaml = file.getYml();

        boolean isFillEnabled = yaml.getBoolean(path + ".fillMenu.enabled", false);
        if (isFillEnabled) {
            String rawMaterial = yaml.getString(path + ".fillMenu.fillItem", "STONE");;
            Optional<XMaterial> optMaterial = XMaterial.matchXMaterial(rawMaterial);
            XMaterial material = optMaterial.orElse(XMaterial.STONE);
            ItemStack itemStack = material.parseItem();
            itemStack = setGUIAction(itemStack, "none", ActionType.FILL, file);

            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName("§7");
            itemStack.setItemMeta(meta);

            for (int slot = 0; slot < inventory.getSize(); slot++) {
                inventory.setItem(slot, itemStack);
            }
        }
    }

    public ItemStack setGUIAction(ItemStack itemStack, String action, ActionType type, ConfigManager file) {
        NBTAPIHook nbtapiHook = litLibs.getNBTAPIHook();
        return nbtapiHook.setGUIAction(action, itemStack, file.getName(), type);
    }

}
