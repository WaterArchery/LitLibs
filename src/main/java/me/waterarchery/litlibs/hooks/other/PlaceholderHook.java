package me.waterarchery.litlibs.hooks.other;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlaceholderHook {

    public static String parsePlaceholders(Player player, String string) {
        if (player != null && string != null && string.contains("%"))
            return PlaceholderAPI.setPlaceholders(player, string);
        else
            return string;
    }

    public static String parseLocalPlaceholders(String string, HashMap<String, Object> placeholders) {
        for (String placeholder : placeholders.keySet()) {
            Object value = placeholders.get(placeholder);
            string = string.replace(placeholder, value.toString());
        }

        return string;
    }

}
