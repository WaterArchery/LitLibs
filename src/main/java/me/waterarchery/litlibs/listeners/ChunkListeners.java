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

public class ChunkListeners implements Listener {

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onNpcLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        int x = chunk.getX();
        int z = chunk.getZ();
        String world = chunk.getWorld().getName();

        NPCHandler npcHandler = NPCHandler.getInstance();

        for (NPC npc : npcHandler.getNpcs()) {
            if (world.equalsIgnoreCase(npc.getWorldName()) && npc.getChunkX() == x && npc.getChunkZ() == z) {
                npc.setDespawned(false);
            }
        }
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

            for (NPC npc : npcHandler.getNpcs()) {
                if (worldName.equalsIgnoreCase(npc.getWorldName()) && npc.getChunkX() == x && npc.getChunkZ() == z) {
                    npc.despawn();
                }
            }
        }, 10);
    }

}
