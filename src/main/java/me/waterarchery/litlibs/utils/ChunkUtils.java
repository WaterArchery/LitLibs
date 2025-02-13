package me.waterarchery.litlibs.utils;

import org.bukkit.World;

public class ChunkUtils {

    public static boolean isChunkLoaded(World world, int chunkX, int chunkZ) {
        if (world == null) return false;

        return world.isChunkLoaded(chunkX, chunkZ);
    }

}
