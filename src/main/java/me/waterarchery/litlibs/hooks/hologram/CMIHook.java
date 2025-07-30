package me.waterarchery.litlibs.hooks.hologram;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Holograms.CMIHologram;
import me.waterarchery.litlibs.hooks.HologramHook;
import org.bukkit.Location;

import java.util.*;

public class CMIHook extends HologramHook {

    private static CMIHook instance = null;

    public static synchronized CMIHook getInstance() {
        if (instance == null) instance = new CMIHook();

        return instance;
    }

    private CMIHook() { }

    @Override
    public void createHologram(Location loc, List<String> lines) {
        String locString = locationToString(loc);
        CMIHologram oldHologram = CMI.getInstance().getHologramManager().getByName(locString);
        if (oldHologram != null) oldHologram.remove();

        lines = parseColors(lines);

        CMIHologram newHologram = new CMIHologram(locString, loc);
        newHologram.setLines(lines);

        CMI.getInstance().getHologramManager().addHologram(newHologram);
        newHologram.update();
    }

    @Override
    public void updateHologram(Location loc, List<String> lines) {
        String locString = locationToString(loc);
        CMIHologram hologram = CMI.getInstance().getHologramManager().getByName(locString);

        if (hologram != null) {
            lines = parseColors(lines);

            hologram.setLines(lines);
            hologram.update();
        }
    }

    @Override
    public void updateHologram(Location loc, int lineNumber, String line) {
        String locString = locationToString(loc);
        CMIHologram hologram = CMI.getInstance().getHologramManager().getByName(locString);

        if (hologram != null) {
            hologram.setLine(lineNumber, line);
            hologram.update();
        }
    }

    @Override
    public void deleteHologram(Location loc) {
        String locString = locationToString(loc);
        CMIHologram hologram = CMI.getInstance().getHologramManager().getByName(locString);

        if (hologram != null) {
            hologram.remove();
        }
    }

}
