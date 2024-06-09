package me.waterarchery.litlibs.handlers;

import me.waterarchery.litlibs.LitLibs;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;

public class MessageHandler {

    private final LitLibs litLibs;

    public MessageHandler(LitLibs litLibs) { this.litLibs = litLibs; }

    public void sendMessage(CommandSender commandSender, String mes) {
        commandSender.sendMessage(getPrefix() + updateColors(mes));
    }

    public String getPrefix() {
        Plugin provider = litLibs.getPlugin();
        String prefix = provider.getConfig().getString("Prefix");
        return updateColors(prefix);
    }

    public String updateColors(String str) {
        if (str.contains("&#")) {
            final Pattern hexPattern = Pattern.compile("&#" + "([A-Fa-f0-9]{6})");
            Matcher matcher = hexPattern.matcher(str);
            StringBuffer buffer = new StringBuffer(str.length() + 4 * 8);
            while (matcher.find())
            {
                String group = matcher.group(1);
                matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                        + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                        + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                        + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
                );
            }
            str = matcher.appendTail(buffer).toString();
        }

        return str.replace("&", "§");
    }

    public static String updateColorsStatic(String str) {
        return updateColorsStatic(str);
    }

}
