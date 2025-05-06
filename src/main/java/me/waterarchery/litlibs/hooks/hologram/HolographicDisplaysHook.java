package me.waterarchery.litlibs.hooks.hologram;

import lombok.Getter;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.hooks.HologramHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class HolographicDisplaysHook extends HologramHook {

    @Getter
    private static HolographicDisplaysAPI holoApi;
    private static HolographicDisplaysHook instance;

    public static synchronized HolographicDisplaysHook getInstance() {
        if (instance == null) {
            LitLibsPlugin litLibsPlugin = LitLibsPlugin.getInstance();
            holoApi = HolographicDisplaysAPI.get(litLibsPlugin);
            instance = new HolographicDisplaysHook();
        }
        return instance;
    }

    private HolographicDisplaysHook() { }

    @Override
    public void createHologram(Location loc, List<String> lines) {
        Bukkit.getScheduler().runTask(LitLibsPlugin.getInstance(), () -> {
            deleteHologram(loc);
            Hologram hologram = getHoloApi().createHologram(loc);
            for (String text : lines) {
                hologram.getLines().appendText(text);
            }
        });
    }

    @Override
    public void updateHologram(Location loc, List<String> lines) {
        Bukkit.getScheduler().runTask(LitLibsPlugin.getInstance(), () -> {
            for (Hologram hologram : getHoloApi().getHolograms()) {
                Location holoLoc = hologram.getPosition().toLocation();
                if (holoLoc.getWorld().getName().equalsIgnoreCase(loc.getWorld().getName())
                        && holoLoc.distance(loc) < 1.0) {
                    hologram.getLines().clear();
                    for (String lineText : lines) {
                        hologram.getLines().appendText(lineText);
                    }
                }
            }
        });
    }

    @Override
    public void updateHologram(Location loc, int lineNumber, String line) {
        Bukkit.getScheduler().runTask(LitLibsPlugin.getInstance(), () -> {
            for (Hologram hologram : getHoloApi().getHolograms()) {
                Location holoLoc = hologram.getPosition().toLocation();
                if (holoLoc.getWorld().getName().equalsIgnoreCase(loc.getWorld().getName())
                        && holoLoc.distance(loc) < 1.0) {
                    hologram.getLines().remove(lineNumber);
                    hologram.getLines().insertText(lineNumber, line);
                }
            }
        });
    }

    @Override
    public void deleteHologram(Location loc) {
        Bukkit.getScheduler().runTask(LitLibsPlugin.getInstance(), () -> {
            for (Hologram hologram : new ArrayList<>(getHoloApi().getHolograms())) {
                Location holoLoc = hologram.getPosition().toLocation();
                if (holoLoc.getWorld().getName().equalsIgnoreCase(loc.getWorld().getName())
                        && holoLoc.distance(loc) < 1.0) {
                    hologram.delete();
                    break;
                }
            }
        });
    }

}
