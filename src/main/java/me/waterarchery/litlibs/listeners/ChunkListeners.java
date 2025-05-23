package me.waterarchery.litlibs.listeners;

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

    private final ExecutorService chunkThreadPool = Executors.newFixedThreadPool(5);

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onNpcLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        int x = chunk.getX();
        int z = chunk.getZ();
        String world = chunk.getWorld().getName();

        NPCHandler npcHandler = NPCHandler.getInstance();
        boolean nullCheck = false;

        // Copying array list to preventing the concurrent modification
        for (NPC npc : new ArrayList<>(npcHandler.getNpcs())) {
            if (npc == null) {
                nullCheck = true;
                continue;
            }

            if (!npc.isDespawned()) continue;
            if (world.equalsIgnoreCase(npc.getWorldName()) && npc.getChunkX() == x && npc.getChunkZ() == z) npc.setDespawned(false);
        }

        if (nullCheck) npcHandler.getNpcs().remove(null);
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        int x = chunk.getX();
        int z = chunk.getZ();
        String worldName = chunk.getWorld().getName();

        Bukkit.getScheduler().runTaskLaterAsynchronously(LitLibsPlugin.getInstance(), () -> {
            World world = Bukkit.getWorld(worldName);
            if (world != null && world.isChunkLoaded(x, z)) return;

            NPCHandler npcHandler = NPCHandler.getInstance();
            List<NPC> validNpcs = new ArrayList<>(npcHandler.getNpcs()).stream()
                    .filter(npc -> npc != null && worldName.equalsIgnoreCase(npc.getWorldName()) && npc.getChunkX() == x && npc.getChunkZ() == z)
                    .toList();

            validNpcs.forEach(NPC::despawn);
        }, 10);
    }

}
