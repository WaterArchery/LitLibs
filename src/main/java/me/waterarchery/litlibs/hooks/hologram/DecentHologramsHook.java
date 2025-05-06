package me.waterarchery.litlibs.hooks.hologram;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import me.waterarchery.litlibs.hooks.HologramHook;
import me.waterarchery.litlibs.utils.ChatUtils;
import org.bukkit.Location;

import java.util.List;

public class DecentHologramsHook extends HologramHook {

    private static DecentHologramsHook instance = null;

    public static synchronized DecentHologramsHook getInstance() {
        if (instance == null)
            instance = new DecentHologramsHook();

        return instance;
    }

    private DecentHologramsHook() { }

    @Override
    public void createHologram(Location loc, List<String> lines) {
        String locString = locationToString(loc);
        Hologram oldHologram = DHAPI.getHologram(locString);
        if (oldHologram != null) {
            oldHologram.delete();
        }

        lines = parseColors(lines);
        DHAPI.createHologram(locString, loc, lines);
    }

    @Override
    public void updateHologram(Location loc, List<String> lines) {
        String locString = locationToString(loc);
        Hologram hologram = DHAPI.getHologram(locString);

        if (hologram == null) {
            createHologram(loc, lines);
        }
        else {
            HologramPage page = hologram.getPage(0);
            lines = parseColors(lines);

            if (lines.size() == page.getLines().size()) {
                int i = 0;
                for (String lineText : lines) {
                    page.setLine(i, lineText);
                    i++;
                }
            }
            else {
                deleteHologram(loc);
                createHologram(loc, lines);
            }
        }
    }

    @Override
    public void updateHologram(Location loc, int lineNumber, String line) {
        String locString = locationToString(loc);
        Hologram hologram = DHAPI.getHologram(locString);

        if (hologram != null) {
            HologramPage page = hologram.getPage(0);
            line = ChatUtils.colorizeLegacy(line);
            page.setLine(lineNumber, line);
        }
    }

    @Override
    public void deleteHologram(Location loc) {
        DHAPI.removeHologram(locationToString(loc));
    }

}
