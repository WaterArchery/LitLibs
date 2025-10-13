package me.waterarchery.litlibs.hooks.protection;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.hooks.ProtectionHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

public class SuperiorHook implements ProtectionHook, Listener {

    private static SuperiorHook instance = null;

    public static synchronized SuperiorHook getInstance() {
        if (instance == null) {
            instance = new SuperiorHook();
        }
        return instance;
    }

    private SuperiorHook() {
        Bukkit.getServer().getPluginManager().registerEvents(this, LitLibsPlugin.getInstance());
    }

    @Override
    public @Nullable UUID getIslandUUID(Location loc) {
        Island island = SuperiorSkyblockAPI.getIslandAt(loc);
        if (island != null) return island.getUniqueId();
        return null;
    }

    @Override
    public @Nullable UUID getIslandUUID(Player player) {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        if (superiorPlayer != null) {
            Island island = superiorPlayer.getIsland();
            return island.getUniqueId();
        }
        return null;
    }

    @Override
    public @Nullable UUID getOwner(Location loc) {
        Island island = SuperiorSkyblockAPI.getIslandAt(loc);
        if (island != null) return island.getOwner().getUniqueId();
        return null;
    }

    @Override
    public ArrayList<UUID> getMembers(Location loc) {
        Island island = SuperiorSkyblockAPI.getIslandAt(loc);
        ArrayList<UUID> members = new ArrayList<>();
        if (island != null) {
            for (SuperiorPlayer player : island.getIslandMembers()) {
                members.add(player.getUniqueId());
            }
        }

        return members;
    }

    @Override
    public boolean canPlayerBuild(Location loc, Player player) {
        Island island = SuperiorSkyblockAPI.getIslandAt(loc);
        if (island != null) {
            SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
            return (island.getIslandMembers(true).contains(superiorPlayer) || island.getIslandMembers().contains(superiorPlayer) || island.getOwner()
                    .equals(superiorPlayer) || island.getCoopPlayers().contains(superiorPlayer));
        }
        return false;
    }

    @Override
    public boolean hasProtection(Player player) {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        if (superiorPlayer != null) {
            Island island = superiorPlayer.getIsland();
            return island != null;
        }
        return false;
    }

    @Override
    public boolean isInternalChatEnabled(Player player) {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        return superiorPlayer.hasTeamChatEnabled();
    }

}
