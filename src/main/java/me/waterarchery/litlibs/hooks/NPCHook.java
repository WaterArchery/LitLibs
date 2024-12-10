package me.waterarchery.litlibs.hooks;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public interface NPCHook {

    void createNPC(Location loc, String name, EntityType type, @Nullable String skin);

    void setSkin(Location loc, String skin);

    void setType(Location loc, EntityType type);

    void setName(Location loc, String name);

    void setLookingNearby(Location loc, boolean lookingNearby);

    void setSilent(Location loc, boolean isSilent);

    void deleteNPC(Location loc);

}
