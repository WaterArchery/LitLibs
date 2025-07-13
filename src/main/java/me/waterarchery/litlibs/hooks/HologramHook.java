package me.waterarchery.litlibs.hooks;

import lombok.Getter;
import lombok.Setter;
import me.waterarchery.litlibs.utils.ChatUtils;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class HologramHook {

    public abstract void createHologram(Location loc, List<String> lines);

    public abstract void updateHologram(Location loc, List<String> lines);

    public abstract void updateHologram(Location loc, int lineNumber, String line);

    public abstract void deleteHologram(Location loc);

    public List<String> parseColors(List<String> rawLines) {
        List<String> lines = new ArrayList<>();

        rawLines.forEach(line -> {
            List<String> colorCodes = new ArrayList<>();
            int index = 0;
            for (char c : line.toCharArray()) {
                if (c == '§' && line.length() > index + 1) {
                    char code = line.charAt(index + 1);
                    colorCodes.add(String.valueOf(c + code));
                }

                index++;
            }

            for (String colorCode : colorCodes) {
                line = line.replace(colorCode, "");
            }

            line = line.replace("§", "");
            lines.add(ChatUtils.colorizeLegacy(line));
        });

        return lines;
    }

    public String locationToString(Location loc) {
        return loc.getWorld().getName() + "_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
    }

}
