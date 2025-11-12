package me.waterarchery.litlibs.listeners;

import com.tcoded.folialib.FoliaLib;
import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.handlers.NPCHandler;
import me.waterarchery.litlibs.impl.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChunkListeners implements Listener {

    private final ExecutorService chunkThreadPool = Executors.newFixedThreadPool(1);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onNpcLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        int x = chunk.getX();
        int z = chunk.getZ();
        String world = chunk.getWorld().getName();

        chunkThreadPool.submit(() -> {
            boolean nullCheck = false;

            NPCHandler npcHandler = NPCHandler.getInstance();
            for (NPC npc : npcHandler.getNpcs()) {
                if (npc == null) {
                    nullCheck = true;
                    continue;
                }

                if (!npc.isDespawned()) continue;
                if (npc.getChunkX() == x && npc.getChunkZ() == z && world.equals(npc.getWorldName())) npc.setDespawned(false);
            }

            if (nullCheck) npcHandler.getNpcs().remove(null);
        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        int x = chunk.getX();
        int z = chunk.getZ();
        String worldName = chunk.getWorld().getName();

        LitLibsPlugin plugin = LitLibsPlugin.getInstance();
        FoliaLib foliaLib = plugin.getFoliaLib();
        foliaLib.getScheduler().runLaterAsync(() -> {
            World world = Bukkit.getWorld(worldName);
            if (world != null && world.isChunkLoaded(x, z)) return;

            NPCHandler npcHandler = NPCHandler.getInstance();
            List<NPC> validNpcs = new ArrayList<>(npcHandler.getNpcs()).stream()
                    .filter(npc -> npc != null && npc.getChunkX() == x && npc.getChunkZ() == z && worldName.equalsIgnoreCase(npc.getWorldName()))
                    .toList();

            validNpcs.forEach(NPC::despawn);
        }, 10);
    }

}
