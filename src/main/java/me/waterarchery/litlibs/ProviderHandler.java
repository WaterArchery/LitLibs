package me.waterarchery.litlibs;

import me.waterarchery.litlibs.logger.LogSeverity;
import me.waterarchery.litlibs.logger.Logger;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class ProviderHandler {

    private static ProviderHandler instance;
    private final Logger logger;
    private final HashMap<String, LitLibs> loadedPlugins = new HashMap<>();

    protected synchronized static ProviderHandler getInstance() {
        if (instance == null) instance = new ProviderHandler();
        return instance;
    }

    private ProviderHandler() {
        logger = new Logger("LitLibs", false);
    }

    public LitLibs getLibs(Plugin provider) {
        String pluginName = provider.getName();
        HashMap<String, LitLibs> hashMap = getLoadedPlugins();
        getLoadedPlugins().remove(pluginName);
        /*
        for (String name : hashMap.keySet()) {
            if (name.equalsIgnoreCase(pluginName)) {
                return hashMap.get(name);
            }
        }
         */

        LitLibs litLibs = new LitLibs(provider);
        register(provider, litLibs);
        return litLibs;
    }

    public boolean isRegistered(Plugin provider) {
        return getLoadedPlugins().containsKey(provider.getName());
    }

    public void register(Plugin provider, LitLibs litLibs) {
        getLoadedPlugins().put(provider.getName(), litLibs);
        logger.log("New provider: " + provider.getName() + " is hooked into LitLibs!", LogSeverity.NORMAL);
    }

    public void unregister(Plugin provider) {
        getLoadedPlugins().remove(provider.getName());
        logger.log("Provider: " + provider.getName() + " is not hooking LitLibs anymore!", LogSeverity.NORMAL);
    }

    public HashMap<String, LitLibs> getLoadedPlugins() {
        return loadedPlugins;
    }
}
