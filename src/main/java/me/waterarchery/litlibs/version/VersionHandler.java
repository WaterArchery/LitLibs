package me.waterarchery.litlibs.version;

import lombok.Getter;
import me.waterarchery.litlibs.logger.Logger;
import org.bukkit.Bukkit;

@Getter
public class VersionHandler {

    private Version version;
    private static VersionHandler instance;

    public synchronized static VersionHandler getInstance() {
        if (instance == null) instance = new VersionHandler();
        return instance;
    }

    private VersionHandler() {
        assignVersion();
    }

    public void assignVersion() {
        String rawVersion = Bukkit.getBukkitVersion().split("-")[0];
        for (Version versionEnum : Version.values()) {
            if (rawVersion.contains(versionEnum.toString())) {
                version = versionEnum;
                Logger.logMessage("Version found: " + version);
                return;
            }
        }

        if (version == null) {
            Logger.logMessage("Version couldn't found. Raw Version: " + rawVersion);
            version = Version.UNKNOWN;
        }
    }

    public boolean isServerNewerThan(Version version) {
        return this.version.getVersionNumber() >= version.getVersionNumber();
    }

    public boolean isServerOlderThan(Version version) {
        return this.version.getVersionNumber() <= version.getVersionNumber();
    }

}
