package me.waterarchery.litlibs.impl.gui.items;

import dev.triumphteam.gui.guis.GuiItem;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LitMenuItem {

    private final GuiItem guiItem;
    private final int slot;

    public LitMenuItem(GuiItem guiItem, int slot) {
        this.guiItem = guiItem;
        this.slot = slot;
    }

}
