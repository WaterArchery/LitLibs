package me.waterarchery.litlibs.handlers;

import com.cryptomorin.xseries.XSound;
import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.logger.LogSeverity;
import me.waterarchery.litlibs.logger.Logger;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SoundHandler {

    private final LitLibs litLibs;

    public SoundHandler(LitLibs litLibs) { this.litLibs = litLibs; }


    public void sendSound(Player p, String configPath) {
        sendSound(p, configPath, 5);
    }

    public void sendSound(Player p, String configPath, double volume) {
        Plugin provider = litLibs.getPlugin();
        FileConfiguration yaml = provider.getConfig();

        String soundName = yaml.getString(configPath);
        sendRawSound(p, soundName, volume, volume);
    }

    public void sendRawSound(Player p, String soundName, double volume, double pitch) {
        XSound xSound = XSound.valueOf(soundName);
        Sound sound = xSound.parseSound();

        if (sound != null) {
            p.playSound(p.getLocation(), sound, (float) volume, (float) pitch);
        }
        else {
            Logger logger = litLibs.getLogger();
            logger.log("Error in sound : " + soundName, LogSeverity.ERROR);
        }
    }

    public void sendRawSound(Player p, String soundName) {
        sendRawSound(p, soundName, 1, 1);
    }

    public void sendSound(Player p, String soundPath, ConfigManager configFile) {
        sendSound(p, soundPath, configFile, 5);
    }

    public void sendSound(Player p, String soundPath, ConfigManager configFile, double volume) {
        FileConfiguration yaml = configFile.getYml();

        String soundName = yaml.getString(soundPath);
        sendSound(p, soundName, volume);
    }

}
