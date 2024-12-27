package me.waterarchery.litlibs.handlers;

import lombok.Getter;
import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.impl.module.ModuleBase;
import me.waterarchery.litlibs.logger.Logger;
import me.waterarchery.litlibs.utils.ReflectionUtils;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ModuleHandler {

    private static ModuleHandler instance;
    private final HashMap<String, List<ModuleBase>> pluginModuleMap = new HashMap<>();

    public static ModuleHandler getInstance() {
        if (instance == null) instance = new ModuleHandler();

        return instance;
    }

    private ModuleHandler() { }

    public void loadModules(Plugin provider, List<Class<? extends ModuleBase>> moduleClasses) {
        List<ModuleBase> modules = pluginModuleMap.get(provider.getName());
        if (modules == null) {
            pluginModuleMap.put(provider.getName(), new ArrayList<>());
            modules = pluginModuleMap.get(provider.getName());
        }

        modules.forEach(moduleBase -> disableModule(moduleBase, provider));
        moduleClasses.forEach(clazz -> ReflectionUtils.loadClass(clazz, provider, provider));
        modules.forEach(moduleBase -> enableModule(moduleBase, provider));
    }

    public void enableModule(ModuleBase module, Plugin provider) {
        if (getEnabledModules(provider).contains(module)) return;
        if (!module.getConfig().getYml().getBoolean("isEnabled")) return;

        module.onEnable();
        module.setEnabled(true);

        Logger logger = LitLibsPlugin.getInstance().getLitLibsLoggers();
        logger.log(String.format("§aEnabled module %s", module.getName()));
    }

    public void disableModule(ModuleBase module, Plugin provider) {
        if (!getEnabledModules(provider).contains(module)) return;

        module.onDisable();
        module.setEnabled(false);

        Logger logger = LitLibsPlugin.getInstance().getLitLibsLoggers();
        logger.log(String.format("§cDisabled module %s", module.getName()));
    }

    public List<ModuleBase> getEnabledModules(Plugin provider) {
        List<ModuleBase> modules = pluginModuleMap.get(provider.getName());
        if (modules == null) return new ArrayList<>();

        return modules.stream().filter(ModuleBase::isEnabled).collect(Collectors.toList());
    }

    public List<ModuleBase> getDisabledModules(Plugin provider) {
        List<ModuleBase> modules = pluginModuleMap.get(provider.getName());
        if (modules == null) return new ArrayList<>();

        return modules.stream().filter(moduleBase -> !moduleBase.isEnabled()).collect(Collectors.toList());
    }

    public <T extends ModuleBase> T getModule(Class<T> moduleClass, Plugin provider) {
        List<ModuleBase> modules = pluginModuleMap.get(provider.getName());
        if (modules == null) return null;

        ModuleBase result = modules.stream()
                .filter(moduleBase -> moduleClass.isAssignableFrom(moduleBase.getClass()))
                .findFirst().orElse(null);

        return moduleClass.cast(result);
    }

    public List<ModuleBase> getModules(Plugin provider) {
        List<ModuleBase> modules = pluginModuleMap.get(provider.getName());

        if (modules == null) return new ArrayList<>();
        return modules;
    }

}
