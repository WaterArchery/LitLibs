package me.waterarchery.litlibs.impl.gui;

import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.Getter;
import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.handlers.InventoryHandler;
import me.waterarchery.litlibs.hooks.other.PlaceholderHook;
import me.waterarchery.litlibs.inventory.ActionType;
import me.waterarchery.litlibs.impl.gui.items.LitMenuItem;
import me.waterarchery.litlibs.utils.ChatUtils;
import me.waterarchery.litlibs.version.Version;
import me.waterarchery.litlibs.version.VersionHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Getter
public abstract class LitGui extends LitGuiBase {

    protected final String menuId;
    protected final FileConfiguration menuYaml;
    protected final InventoryHandler inventoryHandler;
    protected final VersionHandler versionHandler;
    protected final LitLibs litLibs;
    protected BaseGui cachedGui;

    public LitGui(String menuId, FileConfiguration menuYaml, Plugin plugin) {
        this.menuId = menuId;
        this.menuYaml = menuYaml;

        litLibs = LitLibs.of(plugin);
        inventoryHandler = litLibs.getInventoryHandler();
        versionHandler = VersionHandler.getInstance();

        cachedGui = Gui.gui().title(Component.text(menuId)).rows(1).create();
    }

    public void openAsync(Player player) {
        getGui(player).thenAccept(gui -> {
            Bukkit.getScheduler().runTask(litLibs.getPlugin(), () -> gui.open(player));
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    public CompletableFuture<BaseGui> getGui(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            String invName = menuYaml.getString(menuId + ".name");
            invName = parseColorAndPlaceholders(player, invName);
            int size = menuYaml.getInt(menuId + ".size");

            cachedGui = Gui.gui()
                    .title(Component.text(invName))
                    .rows(size / 9)
                    .create();

            if (getDefaultClickAction() != null) cachedGui.setDefaultClickAction(getDefaultClickAction());
            if (getDefaultTopClickAction() != null) cachedGui.setDefaultTopClickAction(getDefaultTopClickAction());
            if (getCloseGuiAction() != null) cachedGui.setCloseGuiAction(getCloseGuiAction());

            fillGUI();

            for (LitMenuItem item : getMenuItems(player)) {
                if (item.getSlot() != -1) cachedGui.setItem(item.getSlot(), item.getGuiItem());
                else cachedGui.addItem(item.getGuiItem());
            }

            return cachedGui;
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    public List<LitMenuItem> getMenuItems(Player player) {
        List<LitMenuItem> items = new ArrayList<>();

        for (String itemPath : Objects.requireNonNull(menuYaml.getConfigurationSection(getItemSectionPath())).getKeys(false)) {
            LitMenuItem litMenuItem = generateItem(player, itemPath);
            items.add(litMenuItem);
        }

        return items;
    }

    public LitMenuItem generateItem(Player player, String itemPath) {
        int slot = menuYaml.getInt(getItemPath() + itemPath + ".slot", -1);
        int modelData = menuYaml.getInt(getItemPath() + itemPath + ".customModelData", -9999);
        String command = menuYaml.getString(getItemPath() + itemPath + ".command", "");
        String itemName = menuYaml.getString(getItemPath() + itemPath + ".name");
        String rawMaterial = menuYaml.getString(getItemPath() + itemPath + ".material");
        List<String> rawLore = menuYaml.getStringList(getItemPath() + itemPath + ".lore");

        // Parsing placeholders in item name
        itemName = parseColorAndPlaceholders(player, itemName);

        List<String> lore = new ArrayList<>();
        for (String part : rawLore) {
            part = parseColorAndPlaceholders(player, part);
            lore.add(part);
        }

        ItemStack itemStack = inventoryHandler.parseItemStack(rawMaterial);
        if (!command.equalsIgnoreCase(""))
            itemStack = inventoryHandler.setGUIAction(itemStack, command, ActionType.COMMAND, menuId);
        else
            itemStack = inventoryHandler.setGUIAction(itemStack, itemPath, ActionType.PLUGIN, menuId);

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(itemName);
            meta.setLore(lore);
            if (modelData != -9999 && versionHandler.isServerNewerThan(Version.v1_14)) meta.setCustomModelData(modelData);
            itemStack.setItemMeta(meta);
        }

        GuiItem guiItem = ItemBuilder.from(itemStack).asGuiItem();
        setAction(itemPath, guiItem);

        return new LitMenuItem(guiItem, slot);
    }

    public String parseColorAndPlaceholders(Player player, String text) {
        text = ChatUtils.colorizeLegacy(text);
        text = PlaceholderHook.parsePlaceholders(player, text);
        text = PlaceholderHook.parseLocalPlaceholders(text, getPlaceholders());

        return text;
    }

    public void setAction(String action, GuiItem guiItem) {
        GuiAction<InventoryClickEvent> func = premadeGuiActions().get(action);

        if (func != null) {
            guiItem.setAction(func);
        }
    }

    public void fillGUI() {
        if (isFillEnabled()) {
            String rawMaterial = menuYaml.getString(menuId + ".fillMenu.fillItem", "BLACK_STAINED_GLASS_PANE");
            String itemName = menuYaml.getString(menuId + ".fillMenu.itemName", "§7");;
            int modelData = menuYaml.getInt(menuId + ".fillMenu.customModelData", -1);;
            Optional<XMaterial> optMaterial = XMaterial.matchXMaterial(rawMaterial);
            XMaterial material = optMaterial.orElse(XMaterial.STONE);
            ItemStack itemStack = material.parseItem();
            itemStack = inventoryHandler.setGUIAction(itemStack, "none", ActionType.FILL, menuId);

            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatUtils.colorizeLegacy(itemName));
                if (modelData != -1) meta.setCustomModelData(modelData);

                itemStack.setItemMeta(meta);
            }

            cachedGui.getFiller().fill(new GuiItem(itemStack));
        }
    }
    
    public boolean isFillEnabled() {
        return menuYaml.getBoolean(menuId + ".fillMenu.enabled", false);
    }

    public String getItemPath() {
        return menuId + ".items.";
    }

    public String getItemSectionPath() {
        return menuId + ".items";
    }

}
