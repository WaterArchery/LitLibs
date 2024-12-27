package me.waterarchery.litlibs;

import lombok.Getter;
import me.waterarchery.litlibs.handlers.ModuleHandler;
import me.waterarchery.litlibs.impl.module.ModuleBase;
import me.waterarchery.litlibs.logger.LogSeverity;
import me.waterarchery.litlibs.logger.Logger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

@Getter
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
        ModuleHandler moduleHandler = ModuleHandler.getInstance();
        moduleHandler.getModules(provider).forEach(ModuleBase::onDisable);
        moduleHandler.getPluginModuleMap().remove(provider.getName());

        getLoadedPlugins().remove(provider.getName());
        logger.log("Provider: " + provider.getName() + " is not hooking LitLibs anymore!", LogSeverity.NORMAL);
    }

}
