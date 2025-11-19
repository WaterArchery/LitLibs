package me.waterarchery.litlibs.impl.gui;

import dev.triumphteam.gui.components.GuiAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public abstract class LitGuiBase {

    public abstract HashMap<String, String> getPlaceholders();

    public abstract HashMap<String, GuiAction<InventoryClickEvent>> premadeGuiActions();

    public abstract GuiAction<@NotNull InventoryClickEvent> getDefaultClickAction();

    public abstract GuiAction<@NotNull InventoryClickEvent> getDefaultTopClickAction();

    public abstract GuiAction<@NotNull InventoryCloseEvent> getCloseGuiAction();
}
