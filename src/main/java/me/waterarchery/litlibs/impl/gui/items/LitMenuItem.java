package me.waterarchery.litlibs.impl.gui.items;

import dev.triumphteam.gui.guis.GuiItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.waterarchery.litlibs.inventory.ActionType;

import java.util.*;

@Setter
@Getter
@AllArgsConstructor
public class LitMenuItem {

    private final GuiItem guiItem;
    private final List<Integer> slots;
    private final ActionType type;

    public LitMenuItem(GuiItem guiItem, List<Integer> slots) {
        this.guiItem = guiItem;
        this.slots = slots;
        this.type = ActionType.PLUGIN;
    }

    public LitMenuItem(GuiItem guiItem, int slot) {
        this.guiItem = guiItem;
        this.slots = List.of(slot);
        this.type = ActionType.PLUGIN;
    }

}
