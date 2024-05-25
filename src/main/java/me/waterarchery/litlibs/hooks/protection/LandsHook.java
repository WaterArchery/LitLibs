package me.waterarchery.litlibs.hooks.protection;

import me.waterarchery.litlibs.hooks.ProtectionHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

public class LandsHook implements ProtectionHook {

    @Override
    public @Nullable UUID getIslandUUID(Location loc) {
        return null;
    }

    @Override
    public @Nullable UUID getIslandUUID(Player player) {
        return null;
    }

    @Override
    public @Nullable UUID getOwner(Location loc) {
        return null;
    }

    @Override
    public ArrayList<UUID> getMembers(Location loc) {
        return null;
    }

    @Override
    public boolean canPlayerBuild(Location loc, Player player) {
        return false;
    }

    @Override
    public boolean hasProtection(Player player) {
        return false;
    }

}
