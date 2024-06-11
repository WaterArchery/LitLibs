package me.waterarchery.litlibs;

import me.waterarchery.litlibs.listeners.PluginDisabledListener;
import me.waterarchery.litlibs.logger.LogSeverity;
import me.waterarchery.litlibs.logger.Logger;
import me.waterarchery.litlibs.version.VersionHandler;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class LitLibsPlugin extends JavaPlugin {

    private static LitLibsPlugin instance;
    private static VersionHandler versionHandler;

    @Override
    public void onEnable() {
        instance = this;
        new Metrics(LitLibsPlugin.getInstance(), 21481);
        String version = getDescription().getVersion();
        Logger logger = new Logger("LitLibs", false);
        versionHandler = VersionHandler.getInstance();
        logger.log("LitLibs enabled version &av" + version, LogSeverity.NORMAL);
        getServer().getPluginManager().registerEvents(new PluginDisabledListener(), this);
    }

    public static LitLibsPlugin getInstance() {
        return getPlugin(LitLibsPlugin.class);
    }

    public VersionHandler getVersionHandler() { return versionHandler; }

    public ProviderHandler getProviderHandler() { return ProviderHandler.getInstance(); }

}
