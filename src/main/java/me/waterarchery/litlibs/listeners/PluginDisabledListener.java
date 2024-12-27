package me.waterarchery.litlibs.listeners;

import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.ProviderHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginDisabledListener implements Listener {

    /*
     * Unregistering LitLibs instances
     * on plugin disabling.
     *
     * This probably not needed other than
     * plugman support.
     */
    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        ProviderHandler handler = LitLibsPlugin.getInstance().getProviderHandler();
        Plugin provider = event.getPlugin();

        if (handler.isRegistered(provider))
            handler.unregister(provider);
    }

}
