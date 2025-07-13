package me.waterarchery.litlibs.inventory;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import lombok.Getter;
import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.handlers.InventoryHandler;
import me.waterarchery.litlibs.hooks.other.PlaceholderHook;
import me.waterarchery.litlibs.utils.ChatUtils;
import me.waterarchery.litlibs.version.Version;
import me.waterarchery.litlibs.version.VersionHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Getter
@Deprecated
public class InventoryImpl {

    private final int size;
    private final String title;
    private final String path;
    private final ConfigManager file;
    private final LitLibs litLibs;

    public InventoryImpl(ConfigManager file, String path, LitLibs litLibs) {
        this.file = file;
        this.path = path;
        this.litLibs = litLibs;
        title = file.getString(path + ".name");
        size = file.getYml().getInt(path + ".size");
    }

    private void generateItems(Player player, Inventory inventory, HashMap<String, String> placeholders) {
        FileConfiguration yaml = file.getYml();
        VersionHandler versionHandler = VersionHandler.getInstance();
        InventoryHandler inventoryHandler = litLibs.getInventoryHandler();

        for (String itemPath : yaml.getConfigurationSection(path + ".items").getKeys(false)) {
            int slot = yaml.getInt(path + ".items." + itemPath + ".slot");
            int modelData = yaml.getInt(path + ".items." + itemPath + ".customModelData", -9999);
            String action = file.getString(path + ".items." + itemPath + ".action", "NONE");
            String actionType = file.getString(path + ".items." + itemPath + ".actionType", "NONE");
            String itemName = file.getString(path + ".items." + itemPath + ".name");
            String rawMaterial = file.getString(path + ".items." + itemPath + ".material");
            List<String> rawLore = yaml.getStringList(path + ".items." + itemPath + ".lore");

            // Parsing placeholders in item name
            itemName = PlaceholderHook.parsePlaceholders(player, itemName);
            itemName = PlaceholderHook.parseLocalPlaceholders(itemName, placeholders);

            List<String> lore = new ArrayList<>();
            for (String part : rawLore) {
                part = ChatUtils.colorizeLegacy(part);
                part = PlaceholderHook.parsePlaceholders(player, part);
                part = PlaceholderHook.parseLocalPlaceholders(part, placeholders);

                lore.add(part);
            }

            ItemStack itemStack = parseItemStack(rawMaterial);
            if (actionType.equalsIgnoreCase("command") || actionType.equalsIgnoreCase("cmd"))
                itemStack = inventoryHandler.setGUIAction(itemStack, action, ActionType.COMMAND, file.getName());
            else if (actionType.equalsIgnoreCase("")) itemStack = inventoryHandler.setGUIAction(itemStack, action, ActionType.NONE, file.getName());
            else {
                if (action != null && !action.equalsIgnoreCase("none")) {
                    itemStack = inventoryHandler.setGUIAction(itemStack, action, ActionType.PLUGIN, file.getName());
                }
                else {
                    itemStack = inventoryHandler.setGUIAction(itemStack, itemPath, ActionType.PLUGIN, file.getName());
                }
            }

            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(itemName);
            meta.setLore(lore);
            if (modelData != -9999 && versionHandler.isServerNewerThan(Version.v1_14)) meta.setCustomModelData(modelData);
            itemStack.setItemMeta(meta);

            inventory.setItem(slot, itemStack);
        }
    }

    public ItemStack parseItemStack(String rawMaterial) {
        if (rawMaterial.contains("HEAD-")) {
            rawMaterial = rawMaterial.replace("HEAD-", "");
            return XSkull.createItem().profile(Profileable.detect(rawMaterial)).apply();
        }
        else {
            Optional<XMaterial> optMaterial = XMaterial.matchXMaterial(rawMaterial);
            XMaterial material = optMaterial.orElse(XMaterial.STONE);
            return material.parseItem();
        }
    }

    public Inventory generateInventory(Player player) {
        InventoryHandler inventoryHandler = litLibs.getInventoryHandler();
        Inventory inventory = Bukkit.createInventory(null, getSize(), getTitle());
        inventoryHandler.fillGUI(inventory, path, file);
        generateItems(player, inventory, new HashMap<>());
        return inventory;
    }

    public Inventory generateInventory(Player player, HashMap<String, String> placeholders) {
        InventoryHandler inventoryHandler = litLibs.getInventoryHandler();
        Inventory inventory = Bukkit.createInventory(null, getSize(), getTitle());
        inventoryHandler.fillGUI(inventory, path, file);
        generateItems(player, inventory, placeholders);
        return inventory;
    }

}