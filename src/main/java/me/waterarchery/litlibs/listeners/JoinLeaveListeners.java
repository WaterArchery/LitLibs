package me.waterarchery.litlibs.listeners;

import me.waterarchery.litlibs.handlers.NPCHandler;
import me.waterarchery.litlibs.impl.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class JoinLeaveListeners implements Listener {

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        NPCHandler npcHandler = NPCHandler.getInstance();
        Player player = event.getPlayer();

        List<NPC> clone = new ArrayList<>(npcHandler.getNpcs());
        clone.forEach(npc -> {
            if (npc == null) return;

            npc.getSeeingPlayers().remove(player.getUniqueId());
        });
    }

}
