package me.waterarchery.litlibs.inventory;

import com.cryptomorin.xseries.XMaterial;
import dev.dbassett.skullcreator.SkullCreator;
import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.hooks.other.NBTAPIHook;
import me.waterarchery.litlibs.hooks.other.PlaceholderHook;
import me.waterarchery.litlibs.version.Version;
import me.waterarchery.litlibs.version.VersionHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

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

    private void fillGUI(Inventory inventory) {
        FileConfiguration yaml = file.getYml();

        boolean isFillEnabled = yaml.getBoolean(path + ".fillMenu.enabled", false);
        if (isFillEnabled) {
            String rawMaterial = yaml.getString(path + ".fillMenu.fillItem", "STONE");;
            Optional<XMaterial> optMaterial = XMaterial.matchXMaterial(rawMaterial);
            XMaterial material = optMaterial.orElse(XMaterial.STONE);
            ItemStack itemStack = material.parseItem();
            itemStack = setGUIAction(itemStack, "none", ActionType.FILL);

            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName("§7");
            itemStack.setItemMeta(meta);

            for (int slot = 0; slot < inventory.getSize(); slot++) {
                inventory.setItem(slot, itemStack);
            }
        }
    }

    private void generateItems(Player player, Inventory inventory, HashMap<String, Object> placeholders) {
        FileConfiguration yaml = file.getYml();
        VersionHandler versionHandler = VersionHandler.getInstance();

        for (String itemPath : yaml.getConfigurationSection( path + ".items").getKeys(false)) {
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
                part = litLibs.getMessageHandler().updateColors(part);
                part = PlaceholderHook.parsePlaceholders(player, part);
                part = PlaceholderHook.parseLocalPlaceholders(part, placeholders);

                lore.add(part);
            }


            ItemStack itemStack = parseItemStack(rawMaterial);
            if (actionType.equalsIgnoreCase("command") || actionType.equalsIgnoreCase("cmd"))
                itemStack = setGUIAction(itemStack, action, ActionType.COMMAND);
            else if (actionType.equalsIgnoreCase(""))
                itemStack = setGUIAction(itemStack, action, ActionType.NONE);
            else {
                if (action != null && !action.equalsIgnoreCase("none")) {
                    itemStack = setGUIAction(itemStack, action, ActionType.PLUGIN);
                }
                else {
                    itemStack = setGUIAction(itemStack, itemPath, ActionType.PLUGIN);
                }
            }

            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(itemName);
            meta.setLore(lore);
            if (modelData != -9999 && versionHandler.isServerNewerThan(Version.v1_14))
                meta.setCustomModelData(modelData);
            itemStack.setItemMeta(meta);

            inventory.setItem(slot, itemStack);
        }
    }

    public ItemStack parseItemStack(String rawMaterial) {
        if (rawMaterial.contains("HEAD-")) {
            rawMaterial = rawMaterial.replace("HEAD-", "");
            return SkullCreator.itemFromBase64(rawMaterial);
        }
        else {
            Optional<XMaterial> optMaterial = XMaterial.matchXMaterial(rawMaterial);
            XMaterial material = optMaterial.orElse(XMaterial.STONE);
            return material.parseItem();
        }
    }

    public ItemStack setGUIAction(ItemStack itemStack, String action, ActionType type) {
        NBTAPIHook nbtapiHook = litLibs.getNBTAPIHook();
        return nbtapiHook.setGUIAction(action, itemStack, file.getName(), type);
    }

    public Inventory generateInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, getSize(), getTitle());
        fillGUI(inventory);
        generateItems(player, inventory, new HashMap<>());
        return inventory;
    }

    public Inventory generateInventory(Player player, HashMap<String, Object> placeholders) {
        Inventory inventory = Bukkit.createInventory(null, getSize(), getTitle());
        fillGUI(inventory);
        generateItems(player, inventory, placeholders);
        return inventory;
    }

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

}
