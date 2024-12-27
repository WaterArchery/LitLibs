package me.waterarchery.litlibs.impl.module;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.configuration.ConfigPart;

import java.util.List;

public class ModuleConfig extends ConfigManager {

    public ModuleConfig(LitLibs litLibs, String folder, String name, boolean saveAfterLoad, boolean useHeaders) {
        super(litLibs, folder, name, saveAfterLoad, useHeaders);

        addDefaultValues();
    }

    public void addDefaultValues() {
        addDefault(ConfigPart.of("isEnabled", false, List.of("Set true to enable module")));
    }

}
