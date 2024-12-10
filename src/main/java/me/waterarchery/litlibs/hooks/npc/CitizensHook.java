package me.waterarchery.litlibs.hooks.npc;

import me.waterarchery.litlibs.hooks.NPCHook;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public class CitizensHook implements NPCHook {

    private final NPCRegistry registry;

    private static CitizensHook instance = null;

    public static synchronized CitizensHook getInstance(String registeryName) {
        if (instance == null)
            instance = new CitizensHook(registeryName);

        return instance;
    }

    private CitizensHook(String registeryName) {
        registry = CitizensAPI.createInMemoryNPCRegistry(registeryName);
    }

    @Override
    public void createNPC(Location loc, String name, EntityType type, @Nullable String skin) {
        NPC npc = registry.createNPC(type, name);
        npc.spawn(loc);
    }

    @Override
    public void setSkin(Location loc, String skin) {
        NPC npc = getNPC(loc);

        if (npc != null) {
            npc.getOrAddTrait(SkinTrait.class).setSkinName(skin);
        }
    }

    @Override
    public void setType(Location loc, EntityType type) {
        NPC npc = getNPC(loc);

        if (npc != null) {
            npc.setBukkitEntityType(type);
        }
    }

    @Override
    public void setName(Location loc, String name) {
        NPC npc = getNPC(loc);

        if (npc != null) {
            npc.setName(name);
        }
    }

    @Override
    public void setLookingNearby(Location loc, boolean lookingNearby) {
        NPC npc = getNPC(loc);

        if (npc != null) {
            npc.getOrAddTrait(LookClose.class).lookClose(lookingNearby);
        }
    }

    @Override
    public void setSilent(Location loc, boolean isSilent) {

    }

    @Override
    public void deleteNPC(Location loc) {
        NPC npc = getNPC(loc);

        if (npc != null) {
            registry.deregister(npc);
        }
    }

    @Nullable
    public NPC getNPC(Location location){
        for (NPC loopedNPC : registry.sorted()) {
            if (location.equals(loopedNPC.getStoredLocation())) return loopedNPC;
        }

        return null;
    }

}
