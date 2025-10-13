package me.waterarchery.litlibs.hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

public interface ProtectionHook {

    @Nullable UUID getIslandUUID(Location loc);

    @Nullable UUID getIslandUUID(Player player);

    @Nullable UUID getOwner(Location loc);

    ArrayList<UUID> getMembers(Location loc);

    boolean canPlayerBuild(Location loc, Player player);

    boolean hasProtection(Player player);

    boolean isInternalChatEnabled(Player player);

}
