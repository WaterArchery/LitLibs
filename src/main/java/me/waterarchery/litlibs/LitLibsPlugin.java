package me.waterarchery.litlibs;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import me.waterarchery.litlibs.listeners.PacketListeners;
import me.waterarchery.litlibs.listeners.PluginDisabledListener;
import me.waterarchery.litlibs.logger.LogSeverity;
import me.waterarchery.litlibs.logger.Logger;
import me.waterarchery.litlibs.version.VersionHandler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class LitLibsPlugin extends JavaPlugin {

    private static LitLibsPlugin instance;
    private static VersionHandler versionHandler;
    private BukkitAudiences adventure;;
    private Logger litLogger;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().getEventManager().registerListener(new PacketListeners(), PacketListenerPriority.NORMAL);
        PacketEvents.getAPI().init();

        instance = this;
        adventure = BukkitAudiences.create(this);
        new Metrics(LitLibsPlugin.getInstance(), 21481);

        String version = getDescription().getVersion();
        litLogger = new Logger("LitLibs", false);

        versionHandler = VersionHandler.getInstance();
        litLogger.log("LitLibs enabled version &av" + version, LogSeverity.NORMAL);
        getServer().getPluginManager().registerEvents(new PluginDisabledListener(), this);
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().getEventManager().unregisterAllListeners();
        PacketEvents.getAPI().terminate();

        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    public BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    public static LitLibsPlugin getInstance() {
        return getPlugin(LitLibsPlugin.class);
    }

    public ProviderHandler getProviderHandler() { return ProviderHandler.getInstance(); }

}
