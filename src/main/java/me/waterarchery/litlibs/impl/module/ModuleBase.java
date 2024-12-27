package me.waterarchery.litlibs.impl.module;

import lombok.Getter;
import lombok.Setter;
import me.waterarchery.litlibs.LitLibs;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter
public abstract class ModuleBase {

    private final Plugin pluginInstance;
    private final LitLibs litLibs;
    private boolean isEnabled;

    public ModuleBase(Plugin pluginInstance) {
        this.pluginInstance = pluginInstance;
        this.litLibs = LitLibs.of(pluginInstance);

        Bukkit.broadcastMessage("11111111111");
    }

    public abstract void onEnable();
    public abstract void onDisable();
    public abstract String getName();
    public abstract String getIdentifier();
    public abstract String getDescription();
    public abstract ModuleConfig getConfig();

}
