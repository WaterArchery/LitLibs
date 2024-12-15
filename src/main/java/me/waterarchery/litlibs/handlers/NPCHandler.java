package me.waterarchery.litlibs.handlers;

import lombok.Getter;
import lombok.Setter;
import me.waterarchery.litlibs.impl.MobNPC;
import me.waterarchery.litlibs.impl.NPC;
import me.waterarchery.litlibs.impl.PlayerNPC;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class NPCHandler {

    private static NPCHandler instance;
    final List<NPC> npcs = new ArrayList<>();

    public static NPCHandler getInstance() {
        if (instance == null) instance = new NPCHandler();

        return instance;
    }

    private NPCHandler() { }

    public void createNPC(String npcName, EntityType entityType, Location location, Runnable onClickAction) {
        final NPC npc;

        if (entityType == EntityType.PLAYER) npc = new PlayerNPC(npcName, location, onClickAction, "STEVE");
        else npc = new MobNPC(npcName, location, onClickAction, entityType);

        npc.startSeeingTask();
        npcs.add(npc);
    }

    public void deleteNPC(UUID uuid) {
        List<NPC> deletedNpcs = npcs.stream().filter(npc -> npc.getUuid().equals(uuid)).toList();
        deletedNpcs.forEach(npc -> {
            npcs.remove(npc);
            npc.despawn();
        });
    }

}
