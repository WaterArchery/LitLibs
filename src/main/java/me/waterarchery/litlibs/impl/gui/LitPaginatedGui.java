package me.waterarchery.litlibs.impl.gui;

import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import me.waterarchery.litlibs.impl.gui.items.LitMenuItem;
import me.waterarchery.litlibs.utils.ChatUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;

public abstract class LitPaginatedGui extends LitGui {

    public LitPaginatedGui(String menuId, FileConfiguration menuYaml, Plugin plugin) {
        super(menuId, menuYaml, plugin);
    }

    @Override
    public CompletableFuture<BaseGui> getGui(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            String invName = menuYaml.getString(menuId + ".name");
            int size = menuYaml.getInt(menuId + ".size");

            cachedGui = Gui.paginated()
                    .title(ChatUtils.colorize(invName))
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

}
