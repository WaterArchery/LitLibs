package me.waterarchery.litlibs.hooks;

import me.waterarchery.litlibs.utils.ChatUtils;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public abstract class HologramHook {

    public abstract void createHologram(Location loc, List<String> lines);

    public abstract void updateHologram(Location loc, List<String> lines);

    public abstract void updateHologram(Location loc, int lineNumber, String line);

    public abstract void deleteHologram(Location loc);

    public List<String> parseColors(List<String> rawLines) {
        List<String> lines = new ArrayList<>();

        rawLines.forEach(line -> lines.add(ChatUtils.colorizeLegacy(line)));

        return lines;
    }

    public String locationToString(Location loc) {
        return loc.getWorld().getName() + "_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
    }

}
