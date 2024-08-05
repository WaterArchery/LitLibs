package me.waterarchery.litlibs.hooks.other;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlaceholderHook {

    private static boolean isEnabled;

    public static String parsePlaceholders(Player player, String string) {
        if (player != null && string != null && string.contains("%") && isEnabled)
            return PlaceholderAPI.setPlaceholders(player, string);
        else
            return string;
    }

    public static String parseLocalPlaceholders(String string, HashMap<String, Object> placeholders) {
        if (!isEnabled) return string;

        for (String placeholder : placeholders.keySet()) {
            Object value = placeholders.get(placeholder);
            String str = value == null ? "None" : value.toString();
            string = string.replace(placeholder, str);
        }

        return string;
    }

    public static void setIsEnabled(boolean isEnabled) {
        PlaceholderHook.isEnabled = isEnabled;
    }

}
