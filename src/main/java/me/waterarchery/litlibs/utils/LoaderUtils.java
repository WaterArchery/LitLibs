package me.waterarchery.litlibs.utils;

import me.waterarchery.litlibs.LitLibsPlugin;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;

public class LoaderUtils {

    public static void loadLib(Library lib) {
        BukkitLibraryManager manager = LitLibsPlugin.getInstance().getBukkitLibraryManager();
        manager.loadLibrary(lib);
    }

}
