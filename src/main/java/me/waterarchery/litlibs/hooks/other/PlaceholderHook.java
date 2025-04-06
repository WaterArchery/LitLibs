package me.waterarchery.litlibs.hooks.other;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;

public class PlaceholderHook {

    private static boolean isEnabled;

    public static String parsePlaceholders(OfflinePlayer player, String string) {
        if (player != null && string != null && string.contains("%") && isEnabled)
            return PlaceholderAPI.setPlaceholders(player, string);
        else
            return string;
    }

    public static String parseLocalPlaceholders(String string, HashMap<String, String> placeholders) {
        if (placeholders == null) return string;

        for (String placeholder : placeholders.keySet()) {
            String value = placeholders.get(placeholder);
            String str = value == null ? "None" : value;
            string = string.replace(placeholder, str);
        }

        return string;
    }

    public static void setIsEnabled(boolean isEnabled) {
        PlaceholderHook.isEnabled = isEnabled;
    }

}
