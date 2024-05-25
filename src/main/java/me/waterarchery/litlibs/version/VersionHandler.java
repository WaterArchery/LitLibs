package me.waterarchery.litlibs.version;

import org.bukkit.Bukkit;

public class VersionHandler {

    private Version version;
    private static VersionHandler instance;

    public synchronized static VersionHandler getInstance() {
        if (instance == null) instance = new VersionHandler();
        return instance;
    }

    private VersionHandler() { assignVersion(); }

    public void assignVersion() {
        String rawVersion = Bukkit.getBukkitVersion().split("-")[0];
        //rawVersion = rawVersion.replace(".", "_");
        //rawVersion = rawVersion.substring(0, rawVersion.lastIndexOf("_"));
        rawVersion = rawVersion.substring(0, rawVersion.lastIndexOf("."));
        for (Version versionEnum : Version.values()) {
            if (versionEnum.toString().equalsIgnoreCase(rawVersion)) {
                version = versionEnum;
            }
        }

        if (version == null)
            version = Version.UNKNOWN;
    }

    public boolean isServerNewerThan(Version version) {
        return this.version.getVersionNumber() >= version.getVersionNumber();
    }

    public boolean isServerOlderThan(Version version) {
        return this.version.getVersionNumber() <= version.getVersionNumber();
    }

    public Version getVersion() {
        return version;
    }

}
