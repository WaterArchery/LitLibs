package me.waterarchery.litlibs.hooks;

import org.bukkit.Location;

import java.util.List;

public interface HologramHook {

    void createHologram(Location loc, List<String> lines);

    void updateHologram(Location loc, List<String> lines);

    void updateHologram(Location loc, int lineNumber, String line);

    void deleteHologram(Location loc);

}
