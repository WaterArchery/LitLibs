package me.waterarchery.litlibs.utils;

import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.handlers.ModuleHandler;
import me.waterarchery.litlibs.impl.module.ModuleBase;
import me.waterarchery.litlibs.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ReflectionUtils {

    public static void loadClass(Class<? extends ModuleBase> moduleBaseClass, Plugin provider, Object... args) {
        Logger logger = LitLibsPlugin.getInstance().getLitLibsLoggers();
        ModuleHandler moduleHandler = ModuleHandler.getInstance();

        Bukkit.broadcastMessage(provider.getClass().getPackageName() + " procidder");

        try {
            Class<?>[] argTypes = Arrays.stream(args)
                    .map(Object::getClass)
                    .toArray(Class<?>[]::new);

            ModuleBase module = moduleBaseClass.getDeclaredConstructor(argTypes).newInstance(args);
            Bukkit.broadcastMessage(module.getClass().getPackageName() + " module");
            logger.log(String.format("Module loaded: %s", module.getName()));
            moduleHandler.getPluginModuleMap().get(provider.getName()).add(module);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

}
