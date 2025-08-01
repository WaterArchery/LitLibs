package me.waterarchery.litlibs;

import com.tcoded.folialib.FoliaLib;
import lombok.Getter;
import me.waterarchery.litlibs.handlers.*;
import me.waterarchery.litlibs.hooks.other.NBTAPIHook;
import me.waterarchery.litlibs.logger.Logger;
import me.waterarchery.litlibs.version.VersionHandler;
import org.bukkit.plugin.Plugin;

@Getter
public class LitLibs {

    public static LitLibs of(Plugin plugin) {
        ProviderHandler handler = ProviderHandler.getInstance();
        return handler.getLibs(plugin);
    }

    private final Plugin plugin;
    private final Logger logger;
    private final MessageHandler messageHandler;
    private final SoundHandler soundHandler;
    private final TitleHandler titleHandler;
    private final InventoryHandler inventoryHandler;
    private final NBTAPIHook nbtapiHook;
    private final FoliaLib foliaLib;
    private HookHandler hookHandler;

    public LitLibs(Plugin plugin) {
        this.plugin = plugin;
        logger = new Logger(plugin.getName(), false);
        messageHandler = new MessageHandler(this);
        soundHandler = new SoundHandler(this);
        titleHandler = new TitleHandler(this);
        inventoryHandler = new InventoryHandler(this);
        nbtapiHook = new NBTAPIHook(this);
        foliaLib = new FoliaLib(plugin);
    }

    public void reload() {
        messageHandler.load();
    }

    public void unregister() {
        ProviderHandler.getInstance().unregister(plugin);
    }

    public VersionHandler getVersionHandler() { return VersionHandler.getInstance(); }

    public ModuleHandler getModuleHandler() { return ModuleHandler.getInstance(); }

    public NBTAPIHook getNBTAPIHook() { return nbtapiHook; }

    public HookHandler getHookHandler() {
        if (hookHandler == null) hookHandler = new HookHandler(this);
        return hookHandler;
    }

}
