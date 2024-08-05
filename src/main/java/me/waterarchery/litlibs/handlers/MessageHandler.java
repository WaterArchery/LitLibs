package me.waterarchery.litlibs.handlers;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;

public class MessageHandler {

    private final LitLibs litLibs;
    private FileConfiguration langYml;

    public MessageHandler(LitLibs litLibs) {
        this.litLibs = litLibs;
        load();
    }

    public void load() {
        Plugin provier = litLibs.getPlugin();
        String lang = provier.getConfig().getString("Language", "en");

        ConfigManager langManager = new ConfigManager(litLibs, "lang", lang, false);
        langYml = langManager.getYml();
    }

    public void sendMessage(CommandSender commandSender, String mes) {
        commandSender.sendMessage(getPrefix() + updateColors(mes));
    }

    public void sendLangMessage(CommandSender commandSender, String path) {
        String mes = langYml.getString(path, "Error: " + path);
        commandSender.sendMessage(getPrefix() + updateColors(mes));
    }

    public String getLangMessage(CommandSender commandSender, String path) {
        String mes = langYml.getString(path, "Error: " + path);
        return updateColors(mes);
    }

    public String getPrefix() {
        Plugin provider = litLibs.getPlugin();
        String prefix = provider.getConfig().getString("Prefix");
        return updateColors(prefix);
    }

    public String updateColors(String str) {
        return MessageHandler.updateColorsStatic(str);
    }

    public static String updateColorsStatic(String str) {
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

}
