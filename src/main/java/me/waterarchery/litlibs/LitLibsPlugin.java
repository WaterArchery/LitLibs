package me.waterarchery.litlibs;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.waterarchery.litlibs.listeners.PacketListeners;
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
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        instance = this;
        new Metrics(LitLibsPlugin.getInstance(), 21481);

        String version = getDescription().getVersion();
        Logger logger = new Logger("LitLibs", false);

        versionHandler = VersionHandler.getInstance();
        logger.log("LitLibs enabled version &av" + version, LogSeverity.NORMAL);
        getServer().getPluginManager().registerEvents(new PluginDisabledListener(), this);

        PacketEvents.getAPI().getEventManager().registerListener(new PacketListeners(), PacketListenerPriority.NORMAL);
        PacketEvents.getAPI().init();
    }

    public static LitLibsPlugin getInstance() {
        return getPlugin(LitLibsPlugin.class);
    }

    public VersionHandler getVersionHandler() { return versionHandler; }

    public ProviderHandler getProviderHandler() { return ProviderHandler.getInstance(); }

}
