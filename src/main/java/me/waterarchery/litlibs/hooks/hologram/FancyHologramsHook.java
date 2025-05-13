package me.waterarchery.litlibs.hooks.hologram;

import de.oliver.fancyholograms.api.FancyHologramsPlugin;
import de.oliver.fancyholograms.api.HologramManager;
import de.oliver.fancyholograms.api.data.TextHologramData;
import de.oliver.fancyholograms.api.hologram.Hologram;
import me.waterarchery.litlibs.hooks.HologramHook;
import me.waterarchery.litlibs.utils.ChatUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FancyHologramsHook extends HologramHook {

    private static FancyHologramsHook instance = null;
    private final HologramManager manager;

    public static synchronized FancyHologramsHook getInstance() {
        if (instance == null)
            instance = new FancyHologramsHook();

        return instance;
    }

    private FancyHologramsHook() {
        manager = FancyHologramsPlugin.get().getHologramManager();;
    }

    @Override
    public void createHologram(Location loc, List<String> lines) {
        String locString = locationToString(loc);
        Optional<Hologram> oldHologram = manager.getHologram(locString);
        oldHologram.ifPresent(Hologram::deleteHologram);

        TextHologramData hologramData = new TextHologramData(locString, loc);
        hologramData.setBillboard(Display.Billboard.VERTICAL);
        hologramData.setTextShadow(true);
        hologramData.setBackground(Color.fromARGB(0, 0, 0, 0));

        lines = parseColors(lines);
        hologramData.setText(lines);

        Hologram hologram = manager.create(hologramData);
        manager.addHologram(hologram);
    }

    @Override
    public void updateHologram(Location loc, List<String> lines) {
        String locString = locationToString(loc);
        Hologram hologram = manager.getHologram(locString).orElse(null);

        if (hologram == null) {
            createHologram(loc, lines);
        }
        else {
            TextHologramData hologramData = (TextHologramData) hologram.getData();
            if (hologramData.getText().size() == lines.size()) {
                lines = parseColors(lines);

                hologramData.setText(lines);
                hologram.queueUpdate();
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
        Hologram hologram = manager.getHologram(locString).orElse(null);

        if (hologram != null) {
            TextHologramData hologramData = (TextHologramData) hologram.getData();
            List<String> lines = new ArrayList<>();

            int i = 0;
            for (String s : hologramData.getText()) {
                if (i == lineNumber) {
                    lines.add(ChatUtils.colorizeLegacy(line));
                }
                else {
                    lines.add(s);
                }
            }

            hologramData.setText(lines);
            hologram.queueUpdate();
        }
    }

    @Override
    public void deleteHologram(Location loc) {
        manager.getHologram(locationToString(loc)).ifPresent(manager::removeHologram);
    }

}
