package me.waterarchery.litlibs.hooks.hologram;

import de.oliver.fancyholograms.api.FancyHologramsPlugin;
import de.oliver.fancyholograms.api.HologramManager;
import de.oliver.fancyholograms.api.data.TextHologramData;
import de.oliver.fancyholograms.api.hologram.Hologram;
import lombok.Getter;
import lombok.Setter;
import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.hooks.HologramHook;
import me.waterarchery.litlibs.utils.ChatUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Display;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Getter
@Setter
public class FancyHologramsHook extends HologramHook {

    private static FancyHologramsHook instance = null;
    private final HologramManager manager;
    private LitLibs litLibs;
    private boolean textShadow;
    private Color backgroundColor;
    private Display.Billboard billboard;

    public static synchronized FancyHologramsHook getInstance() {
        if (instance == null) instance = new FancyHologramsHook(new LitLibs(LitLibsPlugin.getInstance()));

        return instance;
    }

    public FancyHologramsHook(LitLibs litLibs) {
        instance = this;
        this.litLibs = litLibs;
        this.manager = FancyHologramsPlugin.get().getHologramManager();

        reload();
    }

    public void reload() {
        FileConfiguration config = litLibs.getPlugin().getConfig();
        textShadow = config.getBoolean("FancyHolograms.TextShadow", true);

        String rawBillboard = config.getString("FancyHolograms.Billboard", "VERTICAL");
        try {
            billboard = Display.Billboard.valueOf(rawBillboard.toUpperCase(Locale.US));
        } catch (Exception e) {
            litLibs.getLogger().error("Invalid billboard name on FancyHologramsHook: " + rawBillboard);
            billboard = Display.Billboard.VERTICAL;
        }

        int alpha = config.getInt("FancyHolograms.BackgroundColor.Alpha", 0);
        int red = config.getInt("FancyHolograms.BackgroundColor.Red", 0);
        int green = config.getInt("FancyHolograms.BackgroundColor.Green", 0);
        int blue = config.getInt("FancyHolograms.BackgroundColor.Blue", 0);
        backgroundColor = Color.fromARGB(alpha, red, green, blue);
    }

    @Override
    public void createHologram(Location loc, List<String> lines) {
        String locString = locationToString(loc);
        Optional<Hologram> oldHologram = manager.getHologram(locString);
        oldHologram.ifPresent(Hologram::deleteHologram);

        Location holoLoc = loc.clone();
        holoLoc.setYaw(0);
        holoLoc.setPitch(0);

        TextHologramData hologramData = new TextHologramData(locString, holoLoc);
        hologramData.setBillboard(billboard);
        hologramData.setPersistent(false);
        hologramData.setTextShadow(textShadow);
        hologramData.setBackground(backgroundColor);
        hologramData.setTranslation(new Vector3f(0, 0, 0));

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
        } else {
            TextHologramData hologramData = (TextHologramData) hologram.getData();
            if (hologramData.getText().size() == lines.size()) {
                lines = parseColors(lines);

                hologramData.setText(lines);
                hologram.queueUpdate();
            } else {
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
                } else {
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
