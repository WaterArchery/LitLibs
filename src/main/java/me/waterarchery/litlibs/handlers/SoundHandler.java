package me.waterarchery.litlibs.handlers;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.logger.LogSeverity;
import me.waterarchery.litlibs.logger.Logger;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SoundHandler {

    private final LitLibs litLibs;

    public SoundHandler(LitLibs litLibs) { this.litLibs = litLibs; }

    public void sendSound(Player p, String soundPath, ConfigManager configFile) {
        sendSound(p, soundPath, configFile, 5);
    }

    public void sendSound(Player p, String soundPath, ConfigManager configFile, double volume) {
        FileConfiguration yaml = configFile.getYml();

        String soundName = yaml.getString(soundPath);
        try {
            p.playSound(p.getLocation(), Sound.valueOf(soundName), (float) volume, (float) volume);
        }
        catch (Exception e){
            Logger logger = litLibs.getLogger();
            logger.log("Error in sound : " + soundName, LogSeverity.ERROR);
        }
    }
}
