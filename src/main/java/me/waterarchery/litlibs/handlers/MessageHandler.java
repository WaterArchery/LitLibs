package me.waterarchery.litlibs.handlers;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class MessageHandler {

    private final LitLibs litLibs;
    private FileConfiguration langYml;

    public MessageHandler(LitLibs litLibs) {
        this.litLibs = litLibs;
    }

    public void load() {
        Plugin provier = litLibs.getPlugin();
        String lang = provier.getConfig().getString("Language", "en");

        ConfigManager langManager = new ConfigManager(litLibs, "lang", lang, false);
        langYml = langManager.getYml();
    }

    public void sendMessage(CommandSender commandSender, String mes) {
        commandSender.sendMessage(ChatUtils.colorizeLegacy(mes));
    }

    public void sendLangMessage(CommandSender commandSender, String path) {
        String mes = langYml.getString(path, "Error: " + path);
        mes = getPrefix() + mes;

        commandSender.sendMessage(ChatUtils.colorizeLegacy(mes));
    }

    public String getLangMessage(String path) {
        String mes = langYml.getString(path, "Error: " + path);
        return ChatUtils.colorizeLegacy(mes);
    }

    public String getPrefix() {
        Plugin provider = litLibs.getPlugin();
        String prefix = provider.getConfig().getString("Prefix");
        return ChatUtils.colorizeLegacy(prefix);
    }

}
