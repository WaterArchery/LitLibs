package me.waterarchery.litlibs.utils;

import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.handlers.ModuleHandler;
import me.waterarchery.litlibs.impl.module.ModuleBase;
import me.waterarchery.litlibs.logger.Logger;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.*;
import java.util.*;

public class ReflectionUtils {

    public static void loadClass(Class<? extends ModuleBase> moduleBaseClass, Plugin provider, Object... args) {
        Logger logger = LitLibsPlugin.getInstance().getLitLogger();
        ModuleHandler moduleHandler = ModuleHandler.getInstance();

        try {
            Class<?>[] argTypes = Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);

            ModuleBase module = moduleBaseClass.getDeclaredConstructor(argTypes).newInstance(args);
            logger.log(String.format("Module loaded: %s", module.getName()));
            moduleHandler.getPluginModuleMap().get(provider.getName()).add(module);
        }
        catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

}
